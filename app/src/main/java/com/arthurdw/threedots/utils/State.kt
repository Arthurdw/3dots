package com.arthurdw.threedots.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import com.arthurdw.threedots.objects.User

object State {
    val NavController = compositionLocalOf<NavController> { error("No NavController found!") }

    var LocalApiToken: String = ""
        get() {
            if (field.isEmpty()) {
                throw Exception("A request attempt was made for a protected endpoint, but no API token was set!")
            }
            return field
        }

    var LocalUser: User = User.empty()
        get() {
            if (field.id.isEmpty()) {
                throw Exception("Current user is empty!")
            }
            return field
        }
}
