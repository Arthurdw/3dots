package com.arthurdw.threedots.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.ManagedInputField
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State

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
fun SettingsSwitch(onToggle: (Boolean) -> Unit) {
    var state by remember { mutableStateOf(true) }

    Switch(
        checked = state, onCheckedChange = {
            state = it
            onToggle(state)
        }, modifier = Modifier.padding(end = 16.dp), colors = SwitchDefaults.colors(
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
fun SettingsToggle(text: String, onToggle: (Boolean) -> Unit) {
    SplitBetween {
        SettingsText(text)
        SettingsSwitch(onToggle)
    }
}

@Composable
fun SettingsScreen() {
    val user by remember { derivedStateOf { State.LocalUser } }

    ThreeDotsLayout("Settings") {
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
                SettingsText("Change username:", Modifier.padding(bottom = 8.dp))
                ManagedInputField(
                    onComplete = { /* TODO */ },
                    // TODO: Get this name dynamically
                    modifier = Modifier.fillMaxWidth(0.9f),
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsToggle("Notifications:") { /* TODO */ }
                SettingsToggle("Protected:") { /* TODO */ }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                SplitBetween {
                    BaseText("App Version:")
                    // TODO: Get this version number dynamically
                    BaseText("0.0.1")
                }
                SplitBetween {
                    BaseText("User ID:")
                    SmallText(user.id)
                }
            }
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
