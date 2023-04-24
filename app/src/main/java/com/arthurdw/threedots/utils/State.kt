package com.arthurdw.threedots.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }

object State {
    val LocalApiToken: String = ""
        get() {
            if (field.isEmpty()) {
                throw Exception("A request attempt was made for a protected endpoint, but no API token was set!")
            }
            return field
        }
}
