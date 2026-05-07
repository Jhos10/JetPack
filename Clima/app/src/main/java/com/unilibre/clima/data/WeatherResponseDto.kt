package com.unilibre.clima.data

data class WeatherResponseDto(
    val name: String,
    val main: MainDto,
    val weather: List<WeatherDto>,
    val wind: WindDto
)

data class MainDto(
    val temp: Double,
    val humidity: Int
)

data class WeatherDto(
    val main: String
)

data class WindDto(
    val speed: Double
)
