package com.arthurdw.threedots.ui.screens.unlock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.AppContainer
import com.arthurdw.threedots.data.NetworkRepository
import com.arthurdw.threedots.utils.BaseViewModel
import com.arthurdw.threedots.utils.hashString

sealed interface UnlockState {
    object Idle : UnlockState
    object Loading : UnlockState
    object Success : UnlockState
    data class Error(val message: String) : UnlockState
}

class UnlockViewModel(private val container: AppContainer) : BaseViewModel() {
    var state: UnlockState by mutableStateOf(UnlockState.Idle)
        private set

    var token: String by mutableStateOf("")
        private set

    fun unlock(code: String) {
        wrapRepositoryAction({ state = UnlockState.Error(it) }) {
            state = UnlockState.Loading
            val padlock = container.offlineRepository.getPadlockValue()
            if (padlock == null) {
                state = UnlockState.Success
                return@wrapRepositoryAction
            }
            val hashedCode = hashString(code)
            if (padlock != hashedCode) {
                state = UnlockState.Error("Wrong code")
                return@wrapRepositoryAction
            }
            token = hashedCode
            state = UnlockState.Success
        }
    }

    companion object {
        val Factory = createFactoryContainer<UnlockViewModel>()
    }
}
