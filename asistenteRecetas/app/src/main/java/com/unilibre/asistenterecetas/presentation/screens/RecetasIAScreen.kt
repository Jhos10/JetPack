package com.unilibre.asistenterecetas.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.asistenterecetas.presentation.components.TarjetaReceta
import com.unilibre.asistenterecetas.presentation.components.TypewriterText
import com.unilibre.asistenterecetas.presentation.viewmodel.RecetasViewModel
import com.unilibre.asistenterecetas.presentation.viewmodel.UiState

@Composable
fun RecetasIAScreen(
    ingredientes: List<String>,
    viewModel: RecetasViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(ingredientes) {
        viewModel.fetchRecetas(ingredientes)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (state) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        TypewriterText(
                            text = "Consultando a la IA...",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            is UiState.Success -> {
                val recetas = (state as UiState.Success).recetas
                TypewriterText(
                    text = "¡Encontré ${recetas.size} recetas!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn {
                    items(recetas) { receta ->
                        TarjetaReceta(
                            receta = receta,
                            onGuardar = { 
                                viewModel.guardarReceta(it)
                                Toast.makeText(context, "Guardado en Favoritos", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TypewriterText(
                        text = (state as UiState.Error).message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
