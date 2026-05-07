package com.unilibre.finanzasapp.domain.usecase

import com.unilibre.finanzasapp.domain.model.Transaccion
import com.unilibre.finanzasapp.domain.repository.TransaccionRepository
import javax.inject.Inject

class AgregarTransaccionUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    suspend operator fun invoke(t: Transaccion): Result<Unit> {
        if (t.descripcion.isBlank()) return Result.failure(Exception("La descripción no puede estar vacía"))
        if (t.monto <= 0) return Result.failure(Exception("El monto debe ser mayor a 0"))
        if (t.categoria.isBlank()) return Result.failure(Exception("Selecciona una categoría"))
        return runCatching { repository.insertar(t) }
    }
}