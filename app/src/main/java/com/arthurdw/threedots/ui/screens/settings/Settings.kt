package com.arthurdw.threedots.ui.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.components.ManagedInputField
import com.arthurdw.threedots.ui.screens.unlock.UnlockScreen
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.utils.hashSmallString

@Composable
fun BaseText(value: String, modifier: Modifier = Modifier) {
    Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
    )
}

@Composable
fun SmallText(value: String, modifier: Modifier = Modifier) {
    Text(
        text = value,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier,
    )
}

@Composable
fun SettingsText(value: String, modifier: Modifier = Modifier) {
    BaseText(
        value = value,
        modifier = modifier.fillMaxWidth(0.9f)
    )
}

@Composable
fun SettingsSwitch(onToggle: (Boolean) -> Unit, initialValue: Boolean = true) {

    Switch(
        checked = initialValue,
        onCheckedChange = { onToggle(it) },
        modifier = Modifier.padding(end = 16.dp), colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.tertiary,
            uncheckedTrackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
            uncheckedBorderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
        )
    )
}

@Composable
fun SplitBetween(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(0.9f)
    ) {
        content()
    }
}

@Composable
fun SettingsToggle(text: String, initialValue: Boolean = true, onToggle: (Boolean) -> Unit) {
    SplitBetween {
        SettingsText(text)
        SettingsSwitch(onToggle, initialValue)
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory),
) {
    val user by remember { derivedStateOf { State.CurrentUser } }
    val context = LocalContext.current
    var wantToChangePin by remember { mutableStateOf(false) }
    var wantToChangeNotification by remember { mutableStateOf(false) }

    if (wantToChangePin) {
        UnlockScreen(
            text = stringResource(R.string.enter_a_pin_to_protect_your_account),
            onSuccess = {
                val code = hashSmallString(it)
                settingsViewModel.changePin(context, code)
                wantToChangePin = false
            },
            disableCheck = true
        )
        return
    }

    ThreeDotsLayout(stringResource(R.string.settings)) {
        when (settingsViewModel.state) {
            SettingsState.Idle -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        SettingsText(stringResource(R.string.change_username), Modifier.padding(bottom = 8.dp))
                        ManagedInputField(
                            onComplete = { settingsViewModel.changeUsername(context, it) },
                            value = State.CurrentUser.username,
                            modifier = Modifier.fillMaxWidth(0.9f),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        SettingsToggle(
                            stringResource(R.string.notifications),
                            (settingsViewModel.hasNotificationsEnabled)
                        ) {
                            wantToChangeNotification = it
                            settingsViewModel.setNewsNotifications(context, it)
                        }
                        SettingsToggle(stringResource(R.string.padlock), settingsViewModel.hasPadlockEnabled) {
                            wantToChangePin = it
                            if (!it) settingsViewModel.clearPin(context)
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )
                        SplitBetween {
                            BaseText(stringResource(R.string.app_version))
                            // TODO: Get this version number dynamically
                            BaseText("0.0.1")
                        }
                        SplitBetween {
                            BaseText(stringResource(R.string.user_id))
                            SmallText(user.id)
                        }
                    }
                }
            }

            SettingsState.Loading -> Loading()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    PreviewWrapper {
        SettingsScreen()
    }
}
