package com.unilibre.asistenterecetas.presentation.screens

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.asistenterecetas.presentation.viewmodel.CameraViewModel
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    // Este callback es crucial: le avisa a tu NavHost que ya queremos ir a la IA
    onIrARecetas: (List<String>) -> Unit 
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val ingredientes by viewModel.ingredientes.collectAsStateWithLifecycle()
    
    // Un hilo secundario dedicado solo para que ML Kit analice la imagen sin trabar la UI
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    Box(modifier = Modifier.fillMaxSize()) {
        
        // 1. La vista de la cámara usando AndroidView (Interop de Compose)
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { analysis ->
                            analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                viewModel.procesarFrameCamara(imageProxy)
                            }
                        }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalyzer
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. La capa transparente encima de la cámara con los ingredientes
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Apuntando a ingredientes...", color = Color.White, style = MaterialTheme.typography.titleMedium)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Chips horizontales con lo que detecta ML Kit
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ingredientes) { item ->
                    SuggestionChip(
                        onClick = { /* Podrías agregar lógica para borrar un ingrediente si se equivocó */ },
                        label = { Text(item) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón que dispara la navegación hacia RecetasIAScreen
            Button(
                onClick = { onIrARecetas(ingredientes) },
                enabled = ingredientes.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generar Recetas con IA")
            }
        }
    }
}
