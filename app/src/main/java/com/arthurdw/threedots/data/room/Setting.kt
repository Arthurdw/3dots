package com.arthurdw.threedots.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey val token: String,
    val value: String
)