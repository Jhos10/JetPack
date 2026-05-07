package com.unilibre.clima.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.clima.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    // Estado del clima
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    // Historial de búsquedas (Extensión Room)
    val historial: StateFlow<List<CiudadEntity>> = repository.getHistorial()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadWeather(city: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val data = repository.getWeather(city)
                _uiState.value = WeatherUiState.Success(data)
                repository.guardarCiudad(city) // Guardamos en Room al tener éxito
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "Error al cargar el clima")
            }
        }
    }
}
