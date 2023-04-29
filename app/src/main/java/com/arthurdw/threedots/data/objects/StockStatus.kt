package com.arthurdw.threedots.data.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockStatusPicked(
    @SerialName("stock")
    val symbol: String,
    val stockName: String,
    val spent: Float,
    val amount: Float,
)

@Serializable
data class StockStatusFollowed(
    @SerialName("stock")
    val symbol: String,
    val stockName: String,
    val amount: Float,
)

@Serializable
data class StockStatus(
    val picked: StockStatusPicked?,
    val followed: StockStatusFollowed?
)