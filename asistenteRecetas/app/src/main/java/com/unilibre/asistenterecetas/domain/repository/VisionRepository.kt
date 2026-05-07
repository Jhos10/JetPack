package com.unilibre.asistenterecetas.domain.repository

import androidx.camera.core.ImageProxy

interface VisionRepository {
    fun analizarIngredientes(imageProxy: ImageProxy, onResult: (List<String>) -> Unit)
}
