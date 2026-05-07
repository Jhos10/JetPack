package com.unilibre.asistenterecetas.presentation.viewmodel

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import com.unilibre.asistenterecetas.domain.repository.VisionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val visionRepository: VisionRepository
) : ViewModel() {

    private val _ingredientes = MutableStateFlow<List<String>>(emptyList())
    val ingredientes = _ingredientes.asStateFlow()

    fun procesarFrameCamara(imageProxy: ImageProxy) {
        visionRepository.analizarIngredientes(imageProxy) { nuevosIngredientes ->
            // Usamos un Set para evitar que el mismo ingrediente (ej. "Manzana") se repita 10 veces
            val listaActual = _ingredientes.value.toMutableSet()
            listaActual.addAll(nuevosIngredientes)
            _ingredientes.value = listaActual.toList()
        }
    }
}
