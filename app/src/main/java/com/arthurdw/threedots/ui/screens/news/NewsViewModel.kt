package com.arthurdw.threedots.ui.screens.news

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.NetworkRepository
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.utils.BaseViewModel

sealed interface NewsState {
    object Loading : NewsState
    data class Success(val newsItems: List<NewsItem>) : NewsState
    data class Error(val message: String) : NewsState
}

class NewsViewModel(private val networkRepository: NetworkRepository) : BaseViewModel() {
    var state: NewsState by mutableStateOf(NewsState.Loading)
        private set

    init {
        getNews()
    }

    private fun getNews() {
        wrapRepositoryAction({ state = NewsState.Error(it) }) {
            val newsItems = networkRepository.getNews()
            state = NewsState.Success(newsItems)
        }
    }

    companion object {
        val Factory = createFactory<NewsViewModel>()
    }
}
