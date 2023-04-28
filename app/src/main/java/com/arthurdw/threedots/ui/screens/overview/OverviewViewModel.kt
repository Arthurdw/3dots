package com.arthurdw.threedots.ui.screens.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.Repository
import com.arthurdw.threedots.data.objects.PickedStock
import com.arthurdw.threedots.utils.BaseViewModel

sealed interface WorthState {
    object Loading : WorthState
    data class Success(val value: List<Float>) : WorthState
    data class Error(val message: String) : WorthState
}

sealed interface PickedStocksState {
    object Loading : PickedStocksState
    data class Success(val value: List<PickedStock>) : PickedStocksState
    data class Error(val message: String) : PickedStocksState
}

class OverviewViewModel(private val repository: Repository) : BaseViewModel() {
    var worthState: WorthState by mutableStateOf(WorthState.Loading)
        private set

    var pickedStocksState: PickedStocksState by mutableStateOf(PickedStocksState.Loading)
        private set

    init {
        getWorth()
        getStocks()
    }

    private fun getWorth() {
        wrapRepositoryAction({ worthState = WorthState.Error(it) }) {
            worthState = WorthState.Loading
            val worth = repository.getWorth()
            worthState = WorthState.Success(worth)
        }
    }

    private fun getStocks() {
        wrapRepositoryAction({ pickedStocksState = PickedStocksState.Error(it) }) {
            pickedStocksState = PickedStocksState.Loading
            val stocks = repository.getStocks()
            pickedStocksState = PickedStocksState.Success(stocks)
        }
    }


    companion object {
        val Factory = createFactory<OverviewViewModel>()
    }
}