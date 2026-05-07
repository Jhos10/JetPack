package com.unilibre.finanzasapp.domain.model

data class Transaccion(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: String,
    val categoria: String,
    val fecha: Long = System.currentTimeMillis()
)