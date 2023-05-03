package com.arthurdw.threedots.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.arthurdw.threedots.data.objects.User

/**
 * An object containing global state values and objects.
 */
object State {
    /**
     * A compositionLocalOf object that provides access to the navigation controller.
     */
    val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }

    /**
     * A string that represents the API token used for authenticated requests.
     * If this is empty when a request is made, an exception is thrown.
     */
    var ApiToken: String = ""
        get() {
            if (field.isEmpty()) {
                throw Exception("A request attempt was made for a protected endpoint, but no API token was set!")
            }
            return field
        }

    /**
     * A [User] object wrapped in a [mutableStateOf] that can be updated and observed throughout the app.
     */
    var CurrentUser: User by mutableStateOf(User.loading())
}