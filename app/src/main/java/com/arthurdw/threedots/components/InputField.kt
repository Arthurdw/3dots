package com.arthurdw.threedots.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.arthurdw.threedots.utils.empty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagedInputField(
    onComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    value: String = String.empty(),
    colorsOverride: TextFieldColors? = null,
) {
    var query by remember { mutableStateOf(value) }

    InputField(
        value = query,
        onComplete = onComplete,
        placeholder = placeholder,
        onValueChange = { query = it },
        trailingIcon = trailingIcon,
        imeAction = imeAction,
        keyboardType = keyboardType,
        colorsOverride = colorsOverride,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    onComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    value: String = String.empty(),
    colorsOverride: TextFieldColors? = null,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onAny = {
                focusManager.clearFocus()
                onComplete(value)
            }
        ),
        placeholder = { if (placeholder != null) Text(text = placeholder) },
        trailingIcon = trailingIcon,
        colors = colorsOverride ?: TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.primary,
            disabledTextColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            placeholderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent
        ),
        modifier = modifier
    )
}
