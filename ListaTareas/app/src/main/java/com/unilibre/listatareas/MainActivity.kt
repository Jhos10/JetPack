package com.unilibre.listatareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unilibre.listatareas.ui.theme.ListaTareasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaTareasTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    // Elevamos el estado aquí (State Hoisting) 
    var listaTareasGlobal by remember { mutableStateOf(listOf<Tarea>()) }
    var idContador by remember { mutableStateOf(1) }

    NavHost(navController = navController, startDestination = "lista") {
        // Ruta para la lista
        composable("lista") { 
            ListaTareasScreen(
                navController = navController,
                tareas = listaTareasGlobal,
                onAgregarTarea = { nuevoTexto ->
                    if (nuevoTexto.isNotBlank()) {
                        listaTareasGlobal = listaTareasGlobal + Tarea(id = idContador, titulo = nuevoTexto)
                        idContador++
                    }
                },
                onBorrarTarea = { id ->
                    listaTareasGlobal = listaTareasGlobal.filter { it.id != id }
                },
                onToggleTarea = { id, completada ->
                    listaTareasGlobal = listaTareasGlobal.map {
                        if (it.id == id) it.copy(completada = completada) else it
                    }
                }
            )
        }
        
        // Ruta para el detalle con argumento dinámico
        composable(
            route = "detalle/{tareaId}",
            arguments = listOf(navArgument("tareaId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Extraemos el ID del backStackEntry
            val id = backStackEntry.arguments?.getInt("tareaId") ?: 0
            DetalleTareaScreen(tareaId = id, navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTareasPreview() {
    ListaTareasTheme {
        AppNavigation()
    }
}
