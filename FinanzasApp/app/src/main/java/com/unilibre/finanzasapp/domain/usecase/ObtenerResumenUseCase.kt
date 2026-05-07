package com.unilibre.finanzasapp.domain.usecase

import com.unilibre.finanzasapp.domain.repository.TransaccionRepository
import javax.inject.Inject

data class ResumenFinanciero(
    val totalIngresos: Double,
    val totalGastos: Double,
    val balance: Double
)

class ObtenerResumenUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    suspend operator fun invoke(): ResumenFinanciero {
        val ingresos = repository.getTotalPorTipo("INGRESO")
        val gastos = repository.getTotalPorTipo("GASTO")
        return ResumenFinanciero(ingresos, gastos, ingresos - gastos)
    }
}