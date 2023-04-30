package com.arthurdw.threedots.data.objects

import kotlinx.serialization.Serializable

@Serializable
data class StockWorth(
    val symbol: String,
    val stockName: String,
    val price: Float
)