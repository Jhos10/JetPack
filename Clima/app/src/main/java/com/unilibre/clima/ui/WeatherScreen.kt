package com.unilibre.clima.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.clima.data.WeatherData
import com.unilibre.clima.data.WeatherUiState

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historial by viewModel.historial.collectAsStateWithLifecycle()
    var textoBusqueda by remember { mutableStateOf("") }

    val condicionClima = if (uiState is WeatherUiState.Success) {
        (uiState as WeatherUiState.Success).data.condicion
    } else "Clear"

    val targetColor by animateColorAsState(
        targetValue = when (condicionClima) {
            "Rain", "Drizzle" -> Color(0xFF1E3C72)
            "Clear" -> Color(0xFFFF7E5F)
            "Clouds" -> Color(0xFF536976)
            else -> Color(0xFF2C3E50)
        },
        animationSpec = tween(durationMillis = 1000), label = "BgColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(targetColor, Color(0xFF000000))))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.statusBarsPadding()
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar ciudad", color = Color.White) },
                trailingIcon = {
                    IconButton(onClick = { viewModel.loadWeather(textoBusqueda) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (historial.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(historial, key = { it.nombre }) { ciudad ->
                        SuggestionChip(
                            onClick = { viewModel.loadWeather(ciudad.nombre) },
                            label = { Text(ciudad.nombre, color = Color.White) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            when (uiState) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }
                is WeatherUiState.Error -> {
                    Text(
                        text = (uiState as WeatherUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                is WeatherUiState.Success -> {
                    val data = (uiState as WeatherUiState.Success).data
                    WeatherContent(data)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(data: WeatherData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = data.ciudad, style = MaterialTheme.typography.headlineLarge, color = Color.White)
        Text(
            text = "${data.temperatura}°C", 
            style = MaterialTheme.typography.displayLarge, 
            color = Color.White, 
            fontWeight = FontWeight.Bold
        )
        Text(text = data.condicion, style = MaterialTheme.typography.titleLarge, color = Color.LightGray)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ElevatedFilterChip(
                selected = true, onClick = {},
                label = { Text("Humedad: ${data.humedad}%") }
            )
            ElevatedFilterChip(
                selected = true, onClick = {},
                label = { Text("Viento: ${data.viento} m/s") }
            )
        }
    }
}
