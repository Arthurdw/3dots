package com.arthurdw.threedots.ui.screens.unlock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.NetworkRepository
import com.arthurdw.threedots.utils.BaseViewModel

sealed interface UnlockState {
    object Idle : UnlockState
    object Loading : UnlockState
    object Success : UnlockState
    data class Error(val message: String) : UnlockState
}

class UnlockViewModel(private val networkRepository: NetworkRepository) : BaseViewModel() {
    var state: UnlockState by mutableStateOf(UnlockState.Idle)
        private set

    fun unlock(code: String): Boolean {
        // TODO: Check if the code is correct
        return true
    }

    companion object {
        val Factory = createFactory<UnlockViewModel>()
    }
}
