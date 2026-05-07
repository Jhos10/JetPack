package com.unilibre.newsapp.ui.viewmodel

import com.unilibre.newsapp.data.model.Article

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}