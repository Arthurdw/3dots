package com.arthurdw.threedots.ui.screens.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.AppContainer
import com.arthurdw.threedots.utils.BaseViewModel
import com.arthurdw.threedots.utils.State

sealed interface SignInState {
    object Loading : SignInState
    object Waiting : SignInState
    object Success : SignInState
    data class Error(val message: String) : SignInState
}

class SignInViewModel(private val container: AppContainer) : BaseViewModel() {
    var state: SignInState by mutableStateOf(SignInState.Loading)
        private set

    var wasAlreadySignedIn: Boolean by mutableStateOf(true)
        private set

    var hasPadlockEnabled: Boolean by mutableStateOf(false)
        private set

    init {
        tryLastSession()
        checkPadlockEnabled()
    }

    private fun checkPadlockEnabled() {
        wrapRepositoryAction({ Log.e("SignInViewModel", "Failed to check padlock: $it") }) {
            val padlock = container.offlineRepository.getPadlockValue()
            hasPadlockEnabled = !padlock.isNullOrEmpty()
        }
    }

    private suspend fun setLocalUser() {
        State.CurrentUser = container.networkRepository.getMe()
        state = SignInState.Success
    }

    private fun tryLastSession() {
        wrapRepositoryAction({ state = SignInState.Error(it) }) {
            state = SignInState.Loading
            container.offlineRepository.getSessionToken()?.let {
                Log.d("SignInViewModel", "Found session token: $it")
                State.ApiToken = it
                wasAlreadySignedIn = true
                setLocalUser()
            } ?: run {
                state = SignInState.Waiting
                wasAlreadySignedIn = false
            }
        }
    }

    fun signIn(token: String) {
        wrapRepositoryAction({ state = SignInState.Error(it) }) {
            state = SignInState.Loading
            State.ApiToken = container.networkRepository.loginOrRegister(token)
            Log.d("SignInViewModel", "Token: ${State.ApiToken}")
            container.offlineRepository.setSessionToken(State.ApiToken)
            setLocalUser()
        }
    }

    companion object {
        val Factory = createFactoryContainer<SignInViewModel>()
    }
}