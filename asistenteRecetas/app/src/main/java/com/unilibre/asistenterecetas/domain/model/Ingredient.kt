package com.unilibre.asistenterecetas.domain.model

data class Ingredient(
    val name: String,
    val quantity: String? = null,
    val isAvailable: Boolean = true
)
