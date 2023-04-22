package com.arthurdw.threedots.data.objects

import java.util.Date

data class User(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Date,
    val updatedAt: Date,
)