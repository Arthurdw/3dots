package com.arthurdw.threedots.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arthurdw.threedots.ThreeDotsApplication
import com.arthurdw.threedots.data.AppContainer
import com.arthurdw.threedots.data.NetworkRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

private const val UNKNOWN_ERROR = "Unknown error"
private const val BASE_ERROR = "An error occurred while fetching a resource: "

abstract class BaseViewModel : ViewModel() {
    /**
     * Logs an error message and throws an exception with the given message. If [onError] is not
     * an empty lambda, the error message is also passed to that lambda.
     *
     * @param message The error message to log and throw.
     * @param onError A lambda to call with the error message, if not an empty lambda.
     * @throws Exception Always throws an exception with the given message.
     */
    private fun throwException(message: String, onError: (String) -> Unit) {
        val currentClassName = this::class.java.simpleName
        Log.e(currentClassName, message)
        if (onError != {}) onError(message)
        else throw Exception(message)

    }

    /**
     * Wraps the given suspend [action] in a coroutine launched on the current [viewModelScope].
     * If the action throws an [HttpException], logs an error message with the response body, or
     * "Unknown error" if the response body is null. Otherwise, logs an error message with the
     * exception message, or "Unknown error" if the message is null. If [onError] is not an empty
     * lambda, passes the error message to that lambda. Always throws an exception with the error
     * message if [onError] is an empty lambda.
     *
     * @param onError A lambda to call with the error message, if not an empty lambda.
     * @param action The suspend function to wrap in a coroutine.
     * @throws Exception Always throws an exception with the error message if [onError] is an empty
     * lambda.
     */
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
        /**
         * Creates a [ViewModelProvider.Factory] for the given [BaseViewModel] subtype using a
         * [viewModelFactory] builder with a [initializer] that injects a [T] from the
         * [ThreeDotsApplication]'s [AppContainer] into the view model constructor.
         *
         * @param T The type of object to be injected into the view model constructor.
         * @return A [ViewModelProvider.Factory] for the given [BaseViewModel] subtype.
         */
        inline fun <reified VM : BaseViewModel, reified T : Any> createFactory(
            crossinline injectionFunction: (AppContainer) -> T
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as ThreeDotsApplication)
                    val injectionObject = injectionFunction(application.container)
                    VM::class.java.getConstructor(T::class.java).newInstance(injectionObject)
                }
            }
        }

        /**
         * Creates a [ViewModelProvider.Factory] for the given [BaseViewModel] subtype using a
         * [viewModelFactory] builder with a [initializer] that injects a [NetworkRepository] from the
         * [ThreeDotsApplication]'s [AppContainer] into the view model constructor.
         *
         * @return A [ViewModelProvider.Factory] for the given [BaseViewModel] subtype.
         */
        inline fun <reified VM : BaseViewModel> createFactory(): ViewModelProvider.Factory {
            return createFactory<VM, NetworkRepository> { it.networkRepository }
        }

        /**
         * Creates a [ViewModelProvider.Factory] for the given [BaseViewModel] subtype using a
         * [viewModelFactory] builder with a [initializer] that injects an [AppContainer] from the
         * [ThreeDotsApplication] into the view model constructor.
         *
         * @return A [ViewModelProvider.Factory] for the given [BaseViewModel] subtype.
         */
        inline fun <reified VM : BaseViewModel> createFactoryContainer(): ViewModelProvider.Factory {
            return createFactory<VM, AppContainer> { it }
        }
    }
}