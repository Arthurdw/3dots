package com.arthurdw.threedots.data.objects

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
    val token: String
)
