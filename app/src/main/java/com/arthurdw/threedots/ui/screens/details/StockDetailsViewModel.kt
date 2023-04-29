package com.arthurdw.threedots.ui.screens.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.Repository
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.data.objects.StockStatus
import com.arthurdw.threedots.utils.BaseViewModel

sealed interface StockDetailsState {
    object Loading : StockDetailsState
    data class Success(val stock: StockDetails) : StockDetailsState
    data class Error(val message: String) : StockDetailsState
}

sealed interface StockStatusState {
    object Loading : StockStatusState
    data class Success(val status: StockStatus) : StockStatusState
    data class Error(val message: String) : StockStatusState
}

class StockDetailsViewModel(private val repository: Repository) : BaseViewModel() {
    var stockDetailsState: StockDetailsState by mutableStateOf(StockDetailsState.Loading)
        private set

    var stockStatusState: StockStatusState by mutableStateOf(StockStatusState.Loading)
        private set

    fun fetch(symbol: String) {
        getStockDetails(symbol)
        getStockStatus(symbol)
    }

    private fun getStockDetails(symbol: String) {
        wrapRepositoryAction({ stockDetailsState = StockDetailsState.Error(it) }) {
            stockDetailsState = StockDetailsState.Loading
            val stockDetails = repository.getStock(symbol)
            stockDetailsState = StockDetailsState.Success(stockDetails)
        }
    }

    private fun getStockStatus(symbol: String) {
        wrapRepositoryAction({ stockStatusState = StockStatusState.Error(it) }) {
            stockStatusState = StockStatusState.Loading
            val stockStatus = repository.getStockStatus(symbol)
            stockStatusState = StockStatusState.Success(stockStatus)
        }
    }

    companion object {
        val Factory = createFactory<StockDetailsViewModel>()
    }
}
