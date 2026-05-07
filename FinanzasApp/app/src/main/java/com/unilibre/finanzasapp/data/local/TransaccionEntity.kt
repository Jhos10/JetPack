package com.unilibre.finanzasapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: String,        // "INGRESO" | "GASTO"
    val categoria: String,
    val fecha: Long = System.currentTimeMillis()
)