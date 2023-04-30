package com.arthurdw.threedots.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionToken(
    @PrimaryKey val token: String
)

