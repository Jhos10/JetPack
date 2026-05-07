package com.unilibre.asistenterecetas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.unilibre.asistenterecetas.data.local.RecetaDao
import com.unilibre.asistenterecetas.data.local.RecetaEntity
import com.unilibre.asistenterecetas.domain.model.Receta
import com.unilibre.asistenterecetas.domain.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    data class Success(val recetas: List<Receta>) : UiState()
    data class Error(val message: String) : UiState()
}

@HiltViewModel
class RecetasViewModel @Inject constructor(
    private val aiRepository: AiRepository,
    private val recetaDao: RecetaDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchRecetas(ingredientes: List<String>) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val resultados = aiRepository.generarRecetas(ingredientes)
            if (resultados.isNotEmpty()) {
                _uiState.value = UiState.Success(resultados)
            } else {
                _uiState.value = UiState.Error("Error al conectar con la IA. Intenta de nuevo.")
            }
        }
    }

    fun guardarReceta(receta: Receta) {
        viewModelScope.launch {
            val entity = RecetaEntity(
                nombre = receta.nombre,
                tiempoMinutos = receta.tiempo_minutos,
                dificultad = receta.dificultad,
                calorias = receta.calorias,
                pasosJson = Gson().toJson(receta.pasos)
            )
            recetaDao.insertarReceta(entity)
        }
    }
}
