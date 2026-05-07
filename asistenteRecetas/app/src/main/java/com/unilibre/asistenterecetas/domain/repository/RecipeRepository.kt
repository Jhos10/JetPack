package com.unilibre.asistenterecetas.domain.repository

import com.unilibre.asistenterecetas.domain.model.Receta
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getFavoriteRecipes(): Flow<List<Receta>>
    suspend fun insertRecipe(receta: Receta)
    suspend fun deleteRecipe(receta: Receta)
}
