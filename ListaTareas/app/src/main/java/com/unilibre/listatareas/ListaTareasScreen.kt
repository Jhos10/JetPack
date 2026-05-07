package com.unilibre.listatareas

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTareasScreen(
    navController: NavController,
    tareas: List<Tarea>,
    onAgregarTarea: (String) -> Unit,
    onBorrarTarea: (Int) -> Unit,
    onToggleTarea: (Int, Boolean) -> Unit
) {
    var textoTarea by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) } 

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Tareas - Unilibre") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (textoTarea.isBlank()) {
                    mostrarError = true
                } else {
                    onAgregarTarea(textoTarea)
                    textoTarea = "" 
                    mostrarError = false
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            
            OutlinedTextField(
                value = textoTarea,
                onValueChange = { 
                    textoTarea = it
                    if (mostrarError) mostrarError = false 
                },
                label = { Text("Nueva tarea") },
                isError = mostrarError, 
                supportingText = {
                    if (mostrarError) {
                        Text(
                            text = "La tarea no puede estar vacía",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tareas, key = { it.id }) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onCheckedChange = { checked ->
                            onToggleTarea(tarea.id, checked)
                        },
                        onDelete = {
                            onBorrarTarea(tarea.id)
                        },
                        onClick = {
                            navController.navigate("detalle/${tarea.id}")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaItem(
    tarea: Tarea,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    // Manejo del estado del gesto (Swipe)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                onDelete()
                true 
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = if (dismissState.targetValue != SwipeToDismissBoxValue.Settled) 
                    MaterialTheme.colorScheme.error 
                else 
                    Color.Transparent,
                label = "colorAnim"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (dismissState.targetValue != SwipeToDismissBoxValue.Settled) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = tarea.completada, onCheckedChange = onCheckedChange)
                    Text(
                        text = tarea.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                // Botón de eliminar visible nuevamente
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
