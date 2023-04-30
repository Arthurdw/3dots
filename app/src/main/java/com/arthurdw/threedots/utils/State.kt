package com.arthurdw.threedots.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.arthurdw.threedots.data.objects.User

object State {
    val NavController = compositionLocalOf<NavController> { error("No NavController found!") }

    var LocalApiToken: String = ""
        get() {
            if (field.isEmpty()) {
                throw Exception("A request attempt was made for a protected endpoint, but no API token was set!")
            }
            return field
        }

    var LocalUser: User by mutableStateOf(User.loading())
}