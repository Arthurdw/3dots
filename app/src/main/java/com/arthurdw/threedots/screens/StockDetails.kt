package com.arthurdw.threedots.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.data.objects.OwnedStock
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.ui.theme.rememberChartStyle
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.toCurrencyString
import com.arthurdw.threedots.utils.toDateString
import com.arthurdw.threedots.utils.toPercentageString
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import java.util.Date

@Composable
fun StockButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
    ) {
        Text(text)
    }
}

@Composable
fun DataSection(title: String, data: Map<String, String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(0.9f)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        data.forEach { (key, value) ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 0.5.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = key,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = value, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


@Composable
fun StockDetailsScreen(stockSymbol: String) {
    val stock = StockDetails(
        symbol = "IBM",
        assetType = "Common Stock",
        name = "International Business Machines",
        description = "International Business Machines Corporation (IBM) is an American multinational technology company headquartered in Armonk, New York, with operations in over 170 countries. The company began in 1911, founded in Endicott, New York, as the Computing-Tabulating-Recording Company (CTR) and was renamed International Business Machines in 1924. IBM is incorporated in New York. IBM produces and sells computer hardware, middleware and software, and provides hosting and consulting services in areas ranging from mainframe computers to nanotechnology. IBM is also a major research organization, holding the record for most annual U.S. patents generated by a business (as of 2020) for 28 consecutive years. Inventions by IBM include the automated teller machine (ATM), the floppy disk, the hard disk drive, the magnetic stripe card, the relational database, the SQL programming language, the UPC barcode, and dynamic random-access memory (DRAM). The IBM mainframe, exemplified by the System/360, was the dominant computing platform during the 1960s and 1970s.",
        cIK = "51143",
        exchange = "NYSE",
        currency = "USD",
        country = "USA",
        sector = "TECHNOLOGY",
        industry = "COMPUTER & OFFICE EQUIPMENT",
        address = "1 NEW ORCHARD ROAD, ARMONK, NY, US",
        fiscalYearEnd = "December",
        latestQuarter = Date(1672444800),
        marketCapitalization = 116236558000f,
        ebitda = 1.23690004E10f,
        pERatio = "65.71",
        pEGRatio = "1.276",
        bookValue = 24.22f,
        dividendPerShare = 6.59f,
        dividendYield = 0.0515f,
        ePS = "1.95",
        revenuePerShareTTM = 67.06f,
        profitMargin = 0.0271f,
        operatingMarginTTM = 0.125f,
        returnOnAssetsTTM = 0.0365f,
        returnOnEquityTTM = 0.0869f,
        revenueTTM = 60530000000f,
        grossProfitTTM = 3.2688001E10f,
        dilutedEPSTTM = 1.95f,
        quarterlyEarningsGrowthYOY = 0.151f,
        quarterlyRevenueGrowthYOY = 0f,
        analystTargetPrice = 145.94f,
        trailingPE = "65.71",
        forwardPE = "15.55",
        priceToSalesRatioTTM = "2.108",
        priceToBookRatio = "6.75",
        eVToRevenue = "2.969",
        eVToEBITDA = "25.81",
        beta = "0.852",
        fiftyTwoWeekHigh = 151.35f,
        fiftyTwoWeekLow = 112.8f,
        fiftyDayMovingAverage = 130.15f,
        twoHundredDayMovingAverage = 134.19f,
        sharesOutstanding = 9.0710598E8f,
        dividendDate = Date(1696291200),
        exDividendDate = Date(1693612800),
        intraday = listOf(
            128.2000f, 128.2000f, 128.5700f, 128.4600f, 128.3000f, 128.1500f, 128.1400f, 128.1400f,
            128.0015f, 128.1400f, 128.1800f, 127.9450f, 127.8800f, 127.8100f, 127.7800f, 127.6950f,
            127.6500f, 127.5900f, 127.6600f, 127.8000f, 127.7900f, 127.8100f, 127.8100f, 127.8400f,
            127.8100f, 127.8100f, 127.8800f, 127.7900f, 127.7100f, 127.6550f, 127.6500f, 127.5900f,
            127.5500f, 127.5600f, 127.5850f, 127.4800f, 127.4600f, 127.4900f, 127.4200f, 127.4800f,
            127.7100f, 127.7400f, 127.8129f, 127.8200f, 127.7331f, 127.6100f, 127.5400f, 127.6300f,
            127.5800f, 127.6099f, 127.6500f, 127.6700f, 127.6900f, 127.6900f, 127.6300f, 127.5900f,
            127.6700f, 127.7850f, 127.7500f, 127.6500f, 127.7938f, 127.8550f, 127.7800f, 127.7099f,
            127.7700f, 127.8800f, 127.9000f, 127.9000f, 128.0100f, 128.2700f, 128.2900f, 128.5200f,
            128.5300f, 128.5800f, 128.5900f, 128.5800f, 128.4800f, 128.4400f, 128.6200f, 128.7155f,
            129.0850f, 129.1350f, 129.4200f, 129.5950f, 129.8400f, 129.7550f, 129.6699f, 129.4650f,
            128.2900f, 128.1000f, 128.0700f, 127.7700f, 128.0000f, 127.8267f, 128.2900f, 128.1000f,
            128.2000f, 127.7000f, 127.8000f, 127.8600f
        ).reversed(),
        close = 128.2f,
        ask = 128.2f,
        bid = 128.2f,
    )

    val owned = OwnedStock(
        symbol = "IBM",
        amount = 10,
        buyPrice = 1200f
    )

    val scrollState = rememberScrollState()

    val minimumValue = stock.intraday.min()
    val maximumValue = stock.intraday.max()

    ThreeDotsLayout(stockSymbol) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .verticalScroll(scrollState)
        ) {
            ProvideChartStyle(rememberChartStyle()) {
                Chart(
                    chart = lineChart(
                        axisValuesOverrider = AxisValuesOverrider.fixed(
                            minY = minimumValue,
                            maxY = maximumValue
                        ),
                    ),
                    model = stock.entryModel,
                    endAxis = endAxis(),
                    modifier = Modifier.fillMaxWidth(0.9f),
                    chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            DataSection(
                title = "Your stock",
                data = mapOf(
                    "Amount" to owned.amount.toString(),
                    "Buy price" to owned.buyPrice.toCurrencyString(),
                    "Profit" to (stock.price - owned.buyPrice).toCurrencyString()
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            StockButton(text = "PICK", onClick = { /*TODO*/ })
            Spacer(modifier = Modifier.height(8.dp))
            StockButton(text = "SELL", onClick = { /*TODO*/ })
            Spacer(modifier = Modifier.height(8.dp))
            StockButton(text = "Add to watchlist", onClick = { /*TODO*/ })
            Spacer(modifier = Modifier.height(8.dp))
            DataSection(
                title = "General",
                data = mapOf(
                    "Bid" to stock.bid.toCurrencyString(),
                    "Ask" to stock.ask.toCurrencyString(),
                    "Open" to (stock.open?.toCurrencyString() ?: "N/A"),
                    "Previous close" to stock.close.toCurrencyString(),
                    "Today's range" to "${minimumValue.toCurrencyString()} - ${maximumValue.toCurrencyString()}",
                    "52 week range" to "${stock.fiftyTwoWeekLow.toCurrencyString()} - ${stock.fiftyTwoWeekHigh.toCurrencyString()}",
                    "Analyst target price" to (stock.analystTargetPrice?.toCurrencyString() ?: "N/A"),
                    "Market cap" to stock.marketCapitalization.toCurrencyString(),
                    "Revenue" to stock.revenueTTM.toCurrencyString(),
                    "Gross profit" to stock.grossProfitTTM.toCurrencyString(),
                    "EBITDA" to stock.ebitda.toCurrencyString(),
                    "P/E ratio" to stock.pERatio,
                    "P/E/G ratio" to stock.pEGRatio,
                    "EPS" to stock.ePS,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            DataSection(
                title = "Dividend",
                data = mapOf(
                    "Dividend yield" to stock.dividendYield.toPercentageString(),
                    "Dividend P/S" to stock.dividendPerShare.toCurrencyString(),
                    "Ex-dividend date" to stock.exDividendDate.toDateString(),
                    "Dividend date" to stock.dividendDate.toDateString(),
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            DataSection(
                title = "About",
                data = mapOf(
                    "Symbol" to stock.symbol,
                    "Asset Type" to stock.assetType,
                    "Name" to stock.name,
                    "CIK" to stock.cIK,
                    "Exchange" to stock.exchange,
                    "Currency" to stock.currency,
                    "Country" to stock.country,
                    "Sector" to stock.sector,
                    "Industry" to stock.industry,
                    "Address" to stock.address,
                    "Fiscal Year End" to stock.fiscalYearEnd,
                    "Latest Quarter" to stock.latestQuarter.toDateString(),
                    "Description" to ""
                )
            )
            Text(
                text = stock.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DataSection(
                title = "Other details",
                data = mapOf(
                "Fifty day moving average" to stock.fiftyDayMovingAverage.toCurrencyString(),
                "Two hundred day moving average" to stock.twoHundredDayMovingAverage.toCurrencyString(),
                "Shares outstanding" to stock.sharesOutstanding.toCurrencyString(),
                "Book value" to stock.bookValue.toCurrencyString(),
                "Revenue per share TTM" to stock.revenuePerShareTTM.toCurrencyString(),
                "Profit margin" to stock.profitMargin.toPercentageString(),
                "Operating margin TTM" to stock.operatingMarginTTM.toPercentageString(),
                "Return on assets TTM" to stock.returnOnAssetsTTM.toPercentageString(),
                "Return on equity TTM" to stock.returnOnEquityTTM.toPercentageString(),
                "Diluted EPS TTM" to stock.dilutedEPSTTM.toCurrencyString(),
                "Quarterly earnings growth YOY" to stock.quarterlyEarningsGrowthYOY.toPercentageString(),
                "Quarterly revenue growth YOY" to stock.quarterlyRevenueGrowthYOY.toPercentageString(),
                "Trailing PE" to stock.trailingPE,
                "Forward PE" to stock.forwardPE,
                "Price to sales ratio TTM" to stock.priceToSalesRatioTTM,
                "Price to book ratio" to stock.priceToBookRatio,
                "EV to revenue" to stock.eVToRevenue,
                "EV to EBITDA" to stock.eVToEBITDA,
                "Beta" to stock.beta,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockDetailsScreenPreview() {
    PreviewWrapper {
        StockDetailsScreen("AAPL")
    }
}