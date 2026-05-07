package com.unilibre.finanzasapp.data.repository

import com.unilibre.finanzasapp.data.local.TransaccionDao
import com.unilibre.finanzasapp.data.local.TransaccionEntity
import com.unilibre.finanzasapp.domain.model.Transaccion
import com.unilibre.finanzasapp.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransaccionRepositoryImpl @Inject constructor(
    private val dao: TransaccionDao
) : TransaccionRepository {

    override fun getAll(): Flow<List<Transaccion>> =
        dao.getAll().map { lista -> lista.map { it.toDomain() } }

    override suspend fun getTotalPorTipo(tipo: String): Double =
        dao.getTotalPorTipo(tipo) ?: 0.0

    override suspend fun insertar(t: Transaccion) =
        dao.insertar(t.toEntity())

    override suspend fun eliminar(t: Transaccion) =
        dao.eliminar(t.toEntity())
}

private fun TransaccionEntity.toDomain() = Transaccion(id, descripcion, monto, tipo, categoria, fecha)
private fun Transaccion.toEntity() = TransaccionEntity(id, descripcion, monto, tipo, categoria, fecha)