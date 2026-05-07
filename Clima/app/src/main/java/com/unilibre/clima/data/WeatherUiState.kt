package com.unilibre.clima.data

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

data class WeatherData(
    val ciudad: String,
    val temperatura: Double,
    val condicion: String,
    val humedad: Int,
    val viento: Double
)
