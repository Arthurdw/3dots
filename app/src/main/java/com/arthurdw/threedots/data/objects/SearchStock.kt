package com.arthurdw.threedots.data.objects

import kotlinx.serialization.Serializable

// Different serialisation

@Serializable
data class SearchStock(
    val symbol: String,
    val name: String,
    val price: Float,
    val lastPrice: Float
)

fun SearchStock.toBasicStock(): BasicStock = BasicStock(
    symbol = symbol,
    name = name,
    price = price,
    lastPrice = lastPrice
)

fun List<SearchStock>.toBasicStock(): List<BasicStock> = map { it.toBasicStock() }
