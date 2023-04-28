package com.arthurdw.threedots.ui.screens.news

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arthurdw.threedots.ThreeDotsApplication
import com.arthurdw.threedots.data.Repository
import com.arthurdw.threedots.data.objects.NewsItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface NewsState {
    object Loading : NewsState
    data class Success(val newsItems: List<NewsItem>) : NewsState
    data class Error(val message: String) : NewsState
}

class NewsViewModel(private val repository: Repository) : ViewModel() {
    var state: NewsState by mutableStateOf(NewsState.Loading)
        private set

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch {
            state = try {
                try {
                    val newsItems = repository.getNews()
                    NewsState.Success(newsItems)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string() ?: "Unknown error"
                    val message = "An error occurred while fetching the news: $errorBody"
                    Log.e("NewsViewModel", message)
                    NewsState.Error(message)
                }
            } catch (e: Exception) {
                val unknown = "Unknown error"
                val message = "An error occurred while fetching the news: ${e.message ?: unknown}"
                Log.e("NewsViewModel", message)
                NewsState.Error(message)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ThreeDotsApplication)
                val repository = application.container.repository
                NewsViewModel(repository)
            }
        }
    }
}
