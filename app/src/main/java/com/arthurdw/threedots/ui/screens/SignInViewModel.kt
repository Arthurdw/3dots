package com.arthurdw.threedots.ui.screens

import android.util.Log
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
import com.arthurdw.threedots.utils.State
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface SignInState {
    object Loading : SignInState
    object Waiting : SignInState
    object Success : SignInState
    data class Error(val message: String) : SignInState
}

class SignInViewModel(private val repository: Repository) : ViewModel() {
    var state: SignInState by mutableStateOf(SignInState.Waiting)
        private set

    fun signIn(token: String) {
        viewModelScope.launch {
            state = SignInState.Loading
            state = try {
                // TODO: Store data locally
                try {
                    State.LocalApiToken = repository.loginOrRegister(token)
                    State.LocalUser = repository.getMe()
                    SignInState.Success
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string() ?: "Unknown error"
                    val message = "An error occurred while signing in: $errorBody"
                    Log.e("SignInViewModel", message)
                    SignInState.Error(message)
                }
            } catch (e: Exception) {
                val unknown = "Unknown error"
                val message = "An error occurred while signing in: ${e.message ?: unknown}"
                Log.e("SignInViewModel", message)
                SignInState.Error(message)
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