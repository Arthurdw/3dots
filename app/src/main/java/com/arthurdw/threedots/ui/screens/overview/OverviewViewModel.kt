package com.arthurdw.threedots.ui.screens.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.NetworkRepository
import com.arthurdw.threedots.data.objects.BasicStock
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


sealed interface FollowedStocksState {
    object Loading : FollowedStocksState
    data class Success(val value: List<BasicStock>) : FollowedStocksState
    data class Error(val message: String) : FollowedStocksState
}

class OverviewViewModel(private val networkRepository: NetworkRepository) : BaseViewModel() {
    var worthState: WorthState by mutableStateOf(WorthState.Loading)
        private set

    var pickedStocksState: PickedStocksState by mutableStateOf(PickedStocksState.Loading)
        private set

    var followedStocksState: FollowedStocksState by mutableStateOf(FollowedStocksState.Loading)
        private set

    init {
        getWorth()
        getStocks()
        getFollowed()
    }

    private fun getWorth() {
        wrapRepositoryAction({ worthState = WorthState.Error(it) }) {
            worthState = WorthState.Loading
            val worth = networkRepository.getWorth()
            worthState = WorthState.Success(worth)
        }
    }

    private fun getStocks() {
        wrapRepositoryAction({ pickedStocksState = PickedStocksState.Error(it) }) {
            pickedStocksState = PickedStocksState.Loading
            val stocks = networkRepository.getStocks()
            pickedStocksState = PickedStocksState.Success(stocks)
        }
    }

    private fun getFollowed() {
        wrapRepositoryAction({ followedStocksState = FollowedStocksState.Error(it) }) {
            followedStocksState = FollowedStocksState.Loading
            val stocks = networkRepository.getFollowed()
            followedStocksState = FollowedStocksState.Success(stocks)
        }
    }


    companion object {
        val Factory = createFactory<OverviewViewModel>()
    }
}