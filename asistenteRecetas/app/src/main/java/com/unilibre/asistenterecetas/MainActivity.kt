package com.unilibre.asistenterecetas

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.unilibre.asistenterecetas.presentation.screens.CameraScreen
import com.unilibre.asistenterecetas.presentation.screens.RecetasIAScreen
import com.unilibre.asistenterecetas.ui.theme.AsistenteRecetasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsistenteRecetasTheme {
                val navController = rememberNavController()
                val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

                LaunchedEffect(Unit) {
                    if (!cameraPermissionState.status.isGranted) {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (cameraPermissionState.status.isGranted) {
                        NavHost(
                            navController = navController,
                            startDestination = "camera",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("camera") {
                                CameraScreen(onIrARecetas = { ingredientes ->
                                    val ingredientesQuery = ingredientes.joinToString(",")
                                    navController.navigate("recetas/$ingredientesQuery")
                                })
                            }
                            composable(
                                route = "recetas/{ingredientes}",
                                arguments = listOf(navArgument("ingredientes") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val ingredientes = backStackEntry.arguments?.getString("ingredientes")
                                    ?.split(",") ?: emptyList()
                                RecetasIAScreen(ingredientes = ingredientes)
                            }
                        }
                    }
                }
            }
        }
    }
}
