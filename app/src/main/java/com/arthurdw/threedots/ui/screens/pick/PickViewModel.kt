package com.arthurdw.threedots.ui.screens.pick

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.Repository
import com.arthurdw.threedots.data.objects.StockWorth
import com.arthurdw.threedots.utils.BaseViewModel
import com.arthurdw.threedots.utils.State

sealed interface PickState {
    object Loading : PickState
    object Success : PickState
    data class Idle(val stock: StockWorth) : PickState
    data class Error(val message: String) : PickState
}

class PickViewModel(private val repository: Repository) : BaseViewModel() {
    var state: PickState by mutableStateOf(PickState.Loading)
        private set

    private fun performAction(action: suspend () -> Unit) {
        wrapRepositoryAction({ state = PickState.Error(it) }) {
            state = PickState.Loading
            action()
            state = PickState.Success
        }
    }

    fun buyStock(symbol: String, amount: Float) =
        performAction {
            repository.buyStock(symbol, amount)
            State.LocalUser = repository.getMe()
        }

    fun sellStock(symbol: String, amount: Float) =
        performAction {
            repository.sellStock(symbol, amount)
            State.LocalUser = repository.getMe()
        }

    fun initStock(symbol: String) {
        wrapRepositoryAction({ state = PickState.Error(it) }) {
            state = PickState.Loading
            val stock = repository.getStockWorth(symbol)
            state = PickState.Idle(stock)
        }
    }

    companion object {
        val Factory = createFactory<PickViewModel>()
    }
}
