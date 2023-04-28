package com.arthurdw.threedots.data.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PickedStock(
    @SerialName("stock")
    val symbol: String,
    @SerialName("stockName")
    val name: String,
    val amount: Float,
    val spent: Float,
    @SerialName("currentPrice")
    val price: Float,
) {
    fun toBasicStock() = BasicStock(name, symbol, spent, amount * price)
}

fun List<PickedStock>.toBasicStocks() = map { it.toBasicStock() }