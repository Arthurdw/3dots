package com.arthurdw.threedots.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arthurdw.threedots.ThreeDotsApplication
import com.arthurdw.threedots.data.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

private const val UNKNOWN_ERROR = "Unknown error"
private const val BASE_ERROR = "An error occurred while fetching a resource: "

abstract class BaseViewModel<T>(protected val repository: Repository) : ViewModel() {
    private fun throwException(message: String, onError: (String) -> Unit) {
        val currentClassName = this::class.java.simpleName
        Log.e(currentClassName, message)
        if (onError != {}) onError(message)
        else throw Exception(message)

    }

    protected fun wrapRepositoryAction(onError: (String) -> Unit = {}, action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                try {
                    action()
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string() ?: UNKNOWN_ERROR
                    val message = "$BASE_ERROR$errorBody"
                    throwException(message, onError)
                }
            } catch (e: Exception) {
                val message = "$BASE_ERROR${e.message ?: UNKNOWN_ERROR}"
                throwException(message, onError)
            }
        }
    }

    companion object {
        inline fun <reified VM : BaseViewModel<U>, U> createFactory(): ViewModelProvider.Factory {
            Log.d("3dots", "createFactory: ${VM::class.java}")
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as ThreeDotsApplication)
                    val repository = application.container.repository
                    VM::class.java.getConstructor(Repository::class.java).newInstance(repository)
                }
            }
        }
    }
}