package com.unilibre.finanzasapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unilibre.finanzasapp.ui.components.ChipCategoria
import com.unilibre.finanzasapp.presentation.viewmodel.FinanzasViewModel

val CATEGORIAS = listOf("Alimentación", "Transporte", "Salud", "Entretenimiento", "Trabajo", "Otro")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarTransaccionScreen(
    viewModel: FinanzasViewModel,
    onVolver: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("GASTO") }
    var categoria by remember { mutableStateOf("") }

    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva transacción") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto ($)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de tipo
            Text("Tipo", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("INGRESO", "GASTO").forEach { t ->
                    ChipCategoria(t, tipo == t) { tipo = t }
                }
            }

            // Selector de categoría
            Text("Categoría", style = MaterialTheme.typography.titleLarge)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CATEGORIAS.chunked(3).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { cat ->
                            ChipCategoria(cat, categoria == cat) { categoria = cat }
                        }
                    }
                }
            }

            error?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(it, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.error)
                }
            }

            Button(
                onClick = {
                    viewModel.agregar(descripcion, monto, tipo, categoria)
                    if (error == null) onVolver()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar transacción")
            }
        }
    }
}