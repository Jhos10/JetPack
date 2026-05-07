package com.unilibre.finanzasapp.ui.screens

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
import androidx.compose.ui.unit.dp
import com.unilibre.finanzasapp.domain.model.Transaccion
import com.unilibre.finanzasapp.ui.components.TarjetaResumen
import com.unilibre.finanzasapp.ui.theme.Rojo500
import com.unilibre.finanzasapp.ui.theme.Verde500
import com.unilibre.finanzasapp.presentation.viewmodel.FinanzasViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: FinanzasViewModel,
    onAgregar: () -> Unit
) {
    val transacciones by viewModel.transacciones.collectAsState()
    val resumen by viewModel.resumen.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Finanzas Personales") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Icon(Icons.Default.Add, "Agregar")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Tarjetas de resumen
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaResumen("Ingresos", resumen.totalIngresos, Verde500, Modifier.weight(1f))
                    TarjetaResumen("Gastos", resumen.totalGastos, Rojo500, Modifier.weight(1f))
                }
            }

            // Balance
            item {
                val balanceColor = if (resumen.balance >= 0) Verde500 else Rojo500
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = balanceColor.copy(alpha = 0.08f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Balance", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "$ ${"%.2f".format(resumen.balance)}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = balanceColor
                        )
                    }
                }
            }

            // Gráfica de barras
            if (transacciones.isNotEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Ingresos vs Gastos", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(8.dp))
                            GraficaBarras(resumen.totalIngresos, resumen.totalGastos)
                        }
                    }
                }
            }

            // Lista de transacciones
            if (transacciones.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay transacciones aún.\nToca + para agregar una.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                item { Text("Historial", style = MaterialTheme.typography.titleLarge) }
                items(transacciones, key = { it.id }) { t ->
                    ItemTransaccion(t) { viewModel.eliminar(t) }
                }
            }
        }
    }
}

@Composable
fun GraficaBarras(ingresos: Double, gastos: Double) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(ingresos, gastos) {
        modelProducer.runTransaction {
            columnSeries {
                series(ingresos.toFloat(), gastos.toFloat())
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom()
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun ItemTransaccion(t: Transaccion, onEliminar: () -> Unit) {
    val color = if (t.tipo == "INGRESO") Verde500 else Rojo500
    val signo = if (t.tipo == "INGRESO") "+" else "-"
    val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(t.fecha))

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(t.descripcion, style = MaterialTheme.typography.bodyLarge)
                Text(
                    "${t.categoria} · $fecha",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                "$signo$ ${"%.2f".format(t.monto)}",
                color = color,
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}