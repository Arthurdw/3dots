package com.arthurdw.threedots.ui.screens.settings

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.AppContainer
import com.arthurdw.threedots.utils.BaseViewModel

sealed interface SettingsState {
    object Idle : SettingsState
    object Loading : SettingsState
}

class SettingsViewModel(private val container: AppContainer) : BaseViewModel() {
    var state: SettingsState by mutableStateOf(SettingsState.Loading)
        private set

    var hasPadlockEnabled: Boolean by mutableStateOf(false)
        private set

    init {
        checkPadlockEnabled()
    }

    fun changePin(context: Context, code: String) {
        wrapRepositoryAction({ Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }) {
            state = SettingsState.Loading
            container.offlineRepository.setPadlockValue(code)
            checkPadlockEnabled()
        }
    }

    fun clearPin(context: Context) = changePin(context, "")

    private fun checkPadlockEnabled() {
        wrapRepositoryAction({}) {
            val padlockValue = container.offlineRepository.getPadlockValue()
            hasPadlockEnabled = !padlockValue.isNullOrEmpty()
            state = SettingsState.Idle
        }
    }

    companion object {
        val Factory = createFactoryContainer<SettingsViewModel>()
    }
}
