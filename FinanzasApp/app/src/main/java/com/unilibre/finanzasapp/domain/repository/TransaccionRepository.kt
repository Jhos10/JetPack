package com.unilibre.finanzasapp.domain.repository

import com.unilibre.finanzasapp.domain.model.Transaccion
import kotlinx.coroutines.flow.Flow

interface TransaccionRepository {
    fun getAll(): Flow<List<Transaccion>>
    suspend fun getTotalPorTipo(tipo: String): Double
    suspend fun insertar(t: Transaccion)
    suspend fun eliminar(t: Transaccion)
}