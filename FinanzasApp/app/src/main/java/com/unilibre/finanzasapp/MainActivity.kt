package com.unilibre.finanzasapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unilibre.finanzasapp.ui.screens.AgregarTransaccionScreen
import com.unilibre.finanzasapp.ui.screens.DashboardScreen
import com.unilibre.finanzasapp.ui.theme.FinanzasTheme
import com.unilibre.finanzasapp.presentation.viewmodel.FinanzasViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanzasTheme {
                val navController = rememberNavController()
                val viewModel: FinanzasViewModel = hiltViewModel()

                NavHost(navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(viewModel, onAgregar = { navController.navigate("agregar") })
                    }
                    composable("agregar") {
                        AgregarTransaccionScreen(viewModel, onVolver = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}