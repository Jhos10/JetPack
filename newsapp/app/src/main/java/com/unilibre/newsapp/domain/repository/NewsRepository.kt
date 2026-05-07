package com.unilibre.newsapp.domain.repository

import com.unilibre.newsapp.data.model.Article

interface NewsRepository {
    suspend fun getTopHeadlines(): Result<List<Article>>
}