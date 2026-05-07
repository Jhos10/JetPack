package com.unilibre.newsapp.data.repository

import android.util.Log
import com.unilibre.newsapp.data.model.Article
import com.unilibre.newsapp.data.remote.NewsApiService
import com.unilibre.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService
) : NewsRepository {

    override suspend fun getTopHeadlines(): Result<List<Article>> {
        return try {
            val response = api.getTopHeadlines()
            Log.d("NewsRepo", "Code: ${response.code()}")
            Log.d("NewsRepo", "Body: ${response.body()}")
            Log.d("NewsRepo", "Error: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val articles = response.body()?.articles ?: emptyList()
                Result.success(articles)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("NewsRepo", "Exception: ${e.message}")
            Result.failure(e)
        }
    }
}