package com.arthurdw.threedots.ui.screens.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.arthurdw.threedots.data.AppContainer
import com.arthurdw.threedots.utils.BaseViewModel
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.workers.NewsWorker
import java.util.concurrent.TimeUnit

sealed interface SettingsState {
    object Idle : SettingsState
    object Loading : SettingsState
}

class SettingsViewModel(private val container: AppContainer) : BaseViewModel() {
    var state: SettingsState by mutableStateOf(SettingsState.Loading)
        private set

    var hasPadlockEnabled: Boolean by mutableStateOf(false)
        private set

    var hasNotificationsEnabled: Boolean by mutableStateOf(false)
        private set

    private var invocationCount: Int by mutableStateOf(0)

    init {
        checkPadlockEnabled()
        checkNotificationsEnabled()
    }

    fun changePin(context: Context, code: String) {
        wrapRepositoryAction({ Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }) {
            state = SettingsState.Loading
            container.offlineRepository.setPadlockValue(code)
            checkPadlockEnabled()
        }
    }

    fun clearPin(context: Context) = changePin(context, "")

    fun setNewsNotifications(context: Context, enabled: Boolean) {
        wrapRepositoryAction({ Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }) {
            state = SettingsState.Loading
            container.offlineRepository.setNotificationsEnabled(enabled)
            if (enabled) enableNewsNotificationsWorker(context)
            else disableNewsNotificationsWorker(context)
            hasNotificationsEnabled = enabled
            state = SettingsState.Idle
        }
    }

    private fun ensureAllValuesAreSetBeforeIdle() {
        invocationCount++
        if (invocationCount >= 2) {
            state = SettingsState.Idle
        }
    }

    private fun checkPadlockEnabled() {
        wrapRepositoryAction({}) {
            val padlockValue = container.offlineRepository.getPadlockValue()
            hasPadlockEnabled = !padlockValue.isNullOrEmpty()
            ensureAllValuesAreSetBeforeIdle()
        }
    }

    private fun checkNotificationsEnabled() {
        wrapRepositoryAction({}) {
            hasNotificationsEnabled = container.offlineRepository.notificationsEnabled()
            ensureAllValuesAreSetBeforeIdle()
        }
    }


    private fun enableNewsNotificationsWorker(context: Context) {
        if (checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                context as ComponentActivity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        val newsWorker = PeriodicWorkRequestBuilder<NewsWorker>(
            1,
            TimeUnit.HOURS
        ).addTag("news").build()
        WorkManager.getInstance(context).enqueue(newsWorker)
    }

    private fun disableNewsNotificationsWorker(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("news")
    }

    fun changeUsername(context: Context, username: String) {
        wrapRepositoryAction({ Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }) {
            if (username.isEmpty()) {
                Toast.makeText(context, "Username can not be empty!", Toast.LENGTH_LONG).show()
            } else if (username.length > 20) {
                Toast.makeText(
                    context,
                    "Username can not be longer than 20 characters!",
                    Toast.LENGTH_LONG
                ).show()
            } else if (username.length < 3) {
                Toast.makeText(
                    context,
                    "Username can not be shorter than 3 characters!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                state = SettingsState.Loading
                State.CurrentUser = container.networkRepository.changeUsername(username)
                state = SettingsState.Idle
            }
        }
    }

    companion object {
        val Factory = createFactoryContainer<SettingsViewModel>()
    }
}
