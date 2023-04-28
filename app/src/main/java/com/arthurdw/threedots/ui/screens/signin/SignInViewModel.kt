package com.arthurdw.threedots.ui.screens.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arthurdw.threedots.data.Repository
import com.arthurdw.threedots.utils.BaseViewModel
import com.arthurdw.threedots.utils.State

sealed interface SignInState {
    object Loading : SignInState
    object Waiting : SignInState
    object Success : SignInState
    data class Error(val message: String) : SignInState
}

class SignInViewModel(repository: Repository) : BaseViewModel<SignInState>(repository) {
    var state: SignInState by mutableStateOf(SignInState.Waiting)
        private set

    fun signIn(token: String) {
        wrapRepositoryAction({ state = SignInState.Error(it) }) {
            state = SignInState.Loading
            // TODO: Store data locally
            State.LocalApiToken = repository.loginOrRegister(token)
            State.LocalUser = repository.getMe()
            state = SignInState.Success
        }
    }

    companion object {
        val Factory = createFactory<SignInViewModel, SignInState>()
    }
}