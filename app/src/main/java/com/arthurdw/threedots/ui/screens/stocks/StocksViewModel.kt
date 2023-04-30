package com.arthurdw.threedots.ui.screens.stocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.Repository
import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.utils.BaseViewModel

sealed interface StocksState {
    object Loading : StocksState
    data class Success(val stocks: List<BasicStock>) : StocksState
    data class Error(val message: String) : StocksState
}

class StocksViewModel(private val repository: Repository) : BaseViewModel() {
    var state: StocksState by mutableStateOf(StocksState.Loading)
        private set

    init {
        searchStocks("")
    }

    fun searchStocks(query: String) {
        wrapRepositoryAction({ state = StocksState.Error(it) }) {
            state = StocksState.Loading
            val stocks = repository.searchStocks(query)
            state = StocksState.Success(stocks)
        }
    }

    companion object {
        val Factory = createFactory<StocksViewModel>()
    }
}
