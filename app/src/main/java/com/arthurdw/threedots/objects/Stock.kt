package com.arthurdw.threedots.objects

import com.arthurdw.threedots.utils.toPercentageString
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.util.Date
import kotlin.math.roundToInt

abstract class Roi {
    abstract val price: Float
    abstract val lastPrice: Float

    val roi: Float
        get() = (price - lastPrice) / lastPrice * 100

    val roiFormatted: String
        get() = ((roi * 100).roundToInt().toFloat() / 100).toPercentageString()
}

data class BasicStock(
    val name: String,
    val symbol: String,
    override val price: Float,
    override val lastPrice: Float
) : Roi()

data class StockDetails(
    val symbol: String,
    val assetType: String,
    val name: String,
    val description: String,
    val cIK: String,
    val exchange: String,
    val currency: String,
    val country: String,
    val sector: String,
    val industry: String,
    val address: String,
    val fiscalYearEnd: String,
    val latestQuarter: Date,
    val marketCapitalization: Float,
    val ebitda: Float,
    val pERatio: String,
    val pEGRatio: String,
    val dividendPerShare: Float,
    val dividendYield: Float,
    val ePS: String,
    val revenueTTM: Float,
    val grossProfitTTM: Float,
    val analystTargetPrice: Float? = null,
    val fiftyTwoWeekHigh: Float,
    val fiftyTwoWeekLow: Float,
    val dividendDate: Date,
    val exDividendDate: Date,
    val intraday: List<Float>,
    val bid: Float,
    val ask: Float,
    val open: Float? = null,
    val close: Float,
    val fiftyDayMovingAverage: Float,
    val twoHundredDayMovingAverage: Float,
    val sharesOutstanding: Float,
    val bookValue: Float,
    val revenuePerShareTTM: Float,
    val profitMargin: Float,
    val operatingMarginTTM: Float,
    val returnOnAssetsTTM: Float,
    val returnOnEquityTTM: Float,
    val dilutedEPSTTM: Float,
    val quarterlyEarningsGrowthYOY: Float,
    val quarterlyRevenueGrowthYOY: Float,
    val trailingPE: String,
    val forwardPE: String,
    val priceToSalesRatioTTM: String,
    val priceToBookRatio: String,
    val eVToRevenue: String,
    val eVToEBITDA: String,
    val beta: String,
) : Roi() {
    override val price: Float get() = intraday.last()
    override val lastPrice: Float get() = intraday.first()

    val entryModel: ChartEntryModel
        get() = entryModelOf(*intraday.toTypedArray())
}

data class OwnedStock(
    val symbol: String,
    val amount: Int,
    val buyPrice: Float,
)