package com.arthurdw.threedots.data.objects

import com.arthurdw.threedots.utils.DateTimeSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val spent: Float,
    val gained: Float,

    @Serializable(with = DateTimeSerializer::class)
    val createdAt: Date,

    @Serializable(with = DateTimeSerializer::class)
    val updatedAt: Date
) {
    companion object {
        fun loading(): User {
            val loading = "loading..."
            return User(
                id = loading,
                username = loading,
                email = loading,
                spent = 0f,
                gained = 0f,
                createdAt = Date(),
                updatedAt = Date()
            )
        }
    }
}