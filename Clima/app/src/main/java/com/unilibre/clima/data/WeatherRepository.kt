package com.unilibre.clima.data

class WeatherRepository(private val api: WeatherApi, private val dao: HistorialDao) {
    // API KEY gratuita de openweathermap.org
    private val apiKey = "e14acc6a369f3b3b5a740862c5afc2c1"

    suspend fun getWeather(city: String): WeatherData {
        val response = api.getWeather(city, apiKey)
        return WeatherData(
            ciudad = response.name,
            temperatura = response.main.temp,
            condicion = response.weather.firstOrNull()?.main ?: "Clear",
            humedad = response.main.humidity,
            viento = response.wind.speed
        )
    }

    fun getHistorial() = dao.getUltimasCinco()
    suspend fun guardarCiudad(ciudad: String) = dao.insertar(CiudadEntity(nombre = ciudad))
}
