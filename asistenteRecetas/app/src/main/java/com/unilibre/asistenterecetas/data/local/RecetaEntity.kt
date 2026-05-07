package com.unilibre.asistenterecetas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recetas_favoritas")
data class RecetaEntity(
    @PrimaryKey val nombre: String, // Usaremos el nombre como ID para simplificar
    val tiempoMinutos: Int,
    val dificultad: String,
    val calorias: Int,
    val pasosJson: String // Guardamos la lista como un String JSON
)
