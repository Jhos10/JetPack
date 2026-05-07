package com.unilibre.asistenterecetas.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.unilibre.asistenterecetas.domain.model.Receta
import com.unilibre.asistenterecetas.domain.repository.AiRepository
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor() : AiRepository {

    // ⚠️ Reemplaza esto con tu API Key obtenida en Google AI Studio
    private val apiKey = "AIzaSyBlF3A0zxhSY0RffCBxsvAxuBWZnLIRI74"

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = apiKey,
        generationConfig = generationConfig {
            responseMimeType = "application/json" // Forzamos JSON puro
        }
    )

    override suspend fun generarRecetas(ingredientes: List<String>): List<Receta> {
        val prompt = """
            Tengo estos ingredientes: ${ingredientes.joinToString(", ")}.
            Sugiere 3 recetas posibles en formato JSON con los campos:
            nombre (String), tiempo_minutos (Int), dificultad (String), pasos (List<String>), calorias (Int).
            Solo JSON, sin texto adicional.
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonText = response.text ?: "[]"
            
            val listType = object : TypeToken<List<Receta>>() {}.type
            Gson().fromJson(jsonText, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
