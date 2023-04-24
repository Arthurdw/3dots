package com.arthurdw.threedots.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arthurdw.threedots.ThreeDotsApplication
import com.arthurdw.threedots.data.Repository
import com.arthurdw.threedots.objects.User
import com.arthurdw.threedots.utils.State
import kotlinx.coroutines.launch

sealed interface SignInState {
    object Loading : SignInState
    object Waiting : SignInState
    data class Error(val message: String) : SignInState
    data class Success(val user: User) : SignInState
}

class SignInViewModel(private val repository: Repository) : ViewModel() {
    var state: SignInState by mutableStateOf(SignInState.Waiting)
        private set

    fun signIn(token: String) {
        viewModelScope.launch {
            state = SignInState.Loading
            state = try {
                // TODO: Store data locally
                State.LocalApiToken = repository.loginOrRegister(token)
                SignInState.Success(repository.getMe())
            } catch (e: Exception) {
                SignInState.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ThreeDotsApplication)
                val repository = application.container.repository
                SignInViewModel(repository)
            }
        }
    }
}