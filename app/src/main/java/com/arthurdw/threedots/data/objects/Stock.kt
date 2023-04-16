package com.arthurdw.threedots.data.objects

import com.arthurdw.threedots.utils.toPercentageString
import kotlin.math.roundToInt

data class BasicStock(
    val name: String,
    val symbol: String,
    val price: Float,
    val lastPrice: Float
) {
    val roi: Float
        get() = (price - lastPrice) / lastPrice * 100

    val roiFormatted: String
        get() = ((roi * 100).roundToInt().toFloat() / 100).toPercentageString()
}