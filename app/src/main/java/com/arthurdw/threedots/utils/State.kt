package com.arthurdw.threedots.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

object State {
    val NavController = compositionLocalOf<NavController> { error("No NavController found!") }

    val LocalApiToken: String = ""
        get() {
            if (field.isEmpty()) {
                throw Exception("A request attempt was made for a protected endpoint, but no API token was set!")
            }
            return field
        }
}
