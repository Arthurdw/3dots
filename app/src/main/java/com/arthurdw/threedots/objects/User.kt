package com.arthurdw.threedots.objects

import com.arthurdw.threedots.utils.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,

    @Serializable(with = DateSerializer::class)
    val createdAt: Date,

    @Serializable(with = DateSerializer::class)
    val updatedAt: Date
) {
    companion object {
        fun loading(): User {
            val loading = "loading..."
            return User(
                id = loading,
                username = loading,
                email = loading,
                createdAt = Date(),
                updatedAt = Date()
            )
        }
    }
}