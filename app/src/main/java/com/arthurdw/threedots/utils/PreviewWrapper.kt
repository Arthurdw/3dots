package com.arthurdw.threedots.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme


@Composable
internal fun PreviewWrapper(content: @Composable () -> Unit) {
    val navController = rememberNavController()

    ThreeDotsTheme {
        CompositionLocalProvider(State.NavController provides navController) {
            content()
        }
    }
}