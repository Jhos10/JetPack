package com.unilibre.asistenterecetas.domain.model

data class Receta(
    val nombre: String,
    val tiempo_minutos: Int,
    val dificultad: String,
    val pasos: List<String>,
    val calorias: Int
)
