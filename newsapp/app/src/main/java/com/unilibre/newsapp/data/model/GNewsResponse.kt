package com.unilibre.newsapp.data.model

data class GNewsResponse(
    val totalArticles: Int,
    val articles: List<Article>
)