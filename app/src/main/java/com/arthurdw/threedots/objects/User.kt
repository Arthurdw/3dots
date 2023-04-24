package com.arthurdw.threedots.objects

import java.util.Date

data class User(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Date,
    val updatedAt: Date,
) {
    companion object {
        fun empty(): User {
            return User(
                id = "",
                username = "",
                email = "",
                createdAt = Date(),
                updatedAt = Date(),
            )
        }
    }
}