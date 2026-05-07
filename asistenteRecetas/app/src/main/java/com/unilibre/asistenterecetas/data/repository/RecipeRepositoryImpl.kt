package com.unilibre.asistenterecetas.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.unilibre.asistenterecetas.data.local.RecetaDao
import com.unilibre.asistenterecetas.data.local.RecetaEntity
import com.unilibre.asistenterecetas.domain.model.Receta
import com.unilibre.asistenterecetas.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val dao: RecetaDao
) : RecipeRepository {

    override fun getFavoriteRecipes(): Flow<List<Receta>> {
        return dao.getFavoritas().map { entities ->
            entities.map { entity ->
                Receta(
                    nombre = entity.nombre,
                    tiempo_minutos = entity.tiempoMinutos,
                    dificultad = entity.dificultad,
                    calorias = entity.calorias,
                    pasos = try {
                        val listType = object : TypeToken<List<String>>() {}.type
                        Gson().fromJson(entity.pasosJson, listType)
                    } catch (e: Exception) {
                        emptyList()
                    }
                )
            }
        }
    }

    override suspend fun insertRecipe(receta: Receta) {
        val entity = RecetaEntity(
            nombre = receta.nombre,
            tiempoMinutos = receta.tiempo_minutos,
            dificultad = receta.dificultad,
            calorias = receta.calorias,
            pasosJson = Gson().toJson(receta.pasos)
        )
        dao.insertarReceta(entity)
    }

    override suspend fun deleteRecipe(receta: Receta) {
        val entity = RecetaEntity(
            nombre = receta.nombre,
            tiempoMinutos = receta.tiempo_minutos,
            dificultad = receta.dificultad,
            calorias = receta.calorias,
            pasosJson = Gson().toJson(receta.pasos)
        )
        dao.eliminarReceta(entity)
    }
}
