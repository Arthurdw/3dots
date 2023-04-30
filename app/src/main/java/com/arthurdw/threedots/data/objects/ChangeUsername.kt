package com.arthurdw.threedots.data.objects

import kotlinx.serialization.Serializable

@Serializable
data class ChangeUsername(
    val username: String
)