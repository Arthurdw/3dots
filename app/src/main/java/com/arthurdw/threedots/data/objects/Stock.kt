package com.arthurdw.threedots.data.objects

import com.arthurdw.threedots.utils.DateSerializer
import com.arthurdw.threedots.utils.toPercentageString
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
import kotlin.math.roundToInt

abstract class Roi {
    abstract val price: Float
    abstract val lastPrice: Float

    val roi: Float
        get() {
            val estimatedRoi = (price - lastPrice) / lastPrice * 100
            return if (estimatedRoi.isNaN()) 0f else estimatedRoi
        }

    val roiFormatted: String
        get() = ((roi * 100).roundToInt().toFloat() / 100).toPercentageString()
}

@Serializable data class BasicStock(
    @SerialName("stockName")
    val name: String,
    @SerialName("stock")
    val symbol: String,
    override val price: Float,
    override val lastPrice: Float
) : Roi()

@Serializable
data class StockDetails(
    @SerialName("Symbol")
    val symbol: String,
    @SerialName("AssetType")
    val assetType: String,
    @SerialName("Name")
    val name: String,
    @SerialName("Description")
    val description: String,
    @SerialName("CIK")
    val cIK: String,
    @SerialName("Exchange")
    val exchange: String,
    @SerialName("Currency")
    val currency: String,
    @SerialName("Country")
    val country: String,
    @SerialName("Sector")
    val sector: String,
    @SerialName("Industry")
    val industry: String,
    @SerialName("Address")
    val address: String,
    @SerialName("FiscalYearEnd")
    val fiscalYearEnd: String,
    @SerialName("LatestQuarter")
    @Serializable(with = DateSerializer::class)
    val latestQuarter: Date?,
    @SerialName("MarketCapitalization")
    val marketCapitalization: Float,
    @SerialName("EBITDA")
    val ebitda: Float,
    @SerialName("PERatio")
    val pERatio: String,
    @SerialName("PEGRatio")
    val pEGRatio: String,
    @SerialName("DividendPerShare")
    val dividendPerShare: Float,
    @SerialName("DividendYield")
    val dividendYield: Float,
    @SerialName("EPS")
    val ePS: String,
    @SerialName("RevenueTTM")
    val revenueTTM: Float,
    @SerialName("GrossProfitTTM")
    val grossProfitTTM: Float,
    @SerialName("AnalystTargetPrice")
    val analystTargetPrice: Float? = null,
    @SerialName("52WeekHigh")
    val fiftyTwoWeekHigh: Float,
    @SerialName("52WeekLow")
    val fiftyTwoWeekLow: Float,
    @SerialName("DividendDate")
    @Serializable(with = DateSerializer::class)
    val dividendDate: Date?,
    @SerialName("ExDividendDate")
    @Serializable(with = DateSerializer::class)
    val exDividendDate: Date?,
    val intraday: List<Float>,
    val bid: Float,
    val ask: Float,
    val open: Float? = null,
    val close: Float,
    @SerialName("50DayMovingAverage")
    val fiftyDayMovingAverage: Float,
    @SerialName("200DayMovingAverage")
    val twoHundredDayMovingAverage: Float,
    @SerialName("SharesOutstanding")
    val sharesOutstanding: Float,
    @SerialName("BookValue")
    val bookValue: Float,
    @SerialName("RevenuePerShareTTM")
    val revenuePerShareTTM: Float,
    @SerialName("ProfitMargin")
    val profitMargin: Float,
    @SerialName("OperatingMarginTTM")
    val operatingMarginTTM: Float,
    @SerialName("ReturnOnAssetsTTM")
    val returnOnAssetsTTM: Float,
    @SerialName("ReturnOnEquityTTM")
    val returnOnEquityTTM: Float,
    @SerialName("DilutedEPSTTM")
    val dilutedEPSTTM: Float,
    @SerialName("QuarterlyEarningsGrowthYOY")
    val quarterlyEarningsGrowthYOY: Float,
    @SerialName("QuarterlyRevenueGrowthYOY")
    val quarterlyRevenueGrowthYOY: Float,
    @SerialName("TrailingPE")
    val trailingPE: String,
    @SerialName("ForwardPE")
    val forwardPE: String,
    @SerialName("PriceToSalesRatioTTM")
    val priceToSalesRatioTTM: String,
    @SerialName("PriceToBookRatio")
    val priceToBookRatio: String,
    @SerialName("EVToRevenue")
    val eVToRevenue: String,
    @SerialName("EVToEBITDA")
    val eVToEBITDA: String,
    @SerialName("Beta")
    val beta: String,
) : Roi() {
    override val price: Float get() = intraday.last()
    override val lastPrice: Float get() = intraday.first()

    val entryModel: ChartEntryModel
        get() = entryModelOf(*intraday.toTypedArray())
}