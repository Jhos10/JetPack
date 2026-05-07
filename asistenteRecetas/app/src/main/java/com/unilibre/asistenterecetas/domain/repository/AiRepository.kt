package com.unilibre.asistenterecetas.domain.repository

import com.unilibre.asistenterecetas.domain.model.Receta

interface AiRepository {
    suspend fun generarRecetas(ingredientes: List<String>): List<Receta>
}
