package com.arthurdw.threedots.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme

/**
 * Wraps a Composable with the ThreeDotsTheme and provides a NavController using rememberNavController().
 *
 * @param content The Composable to be wrapped.
 */
@Composable
internal fun PreviewWrapper(content: @Composable () -> Unit) {
    val navController = rememberNavController()

    ThreeDotsTheme {
        CompositionLocalProvider(State.LocalNavController provides navController) {
            content()
        }
    }
}