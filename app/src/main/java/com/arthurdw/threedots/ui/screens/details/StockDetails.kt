package com.arthurdw.threedots.ui.screens.details

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.ui.theme.rememberChartStyle
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.utils.toCurrencyString
import com.arthurdw.threedots.utils.toDateString
import com.arthurdw.threedots.utils.toPercentageString
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider

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
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun StockDetailsContent(
    stock: StockDetails,
    stockStatus: StockStatusState,
    onStockFollow: () -> Unit,
    onStockUnfollow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val navController = State.NavController.current

    val minimumValue = stock.intraday.min()
    val maximumValue = stock.intraday.max()

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
        when (stockStatus) {
            is StockStatusState.Error -> Text(stockStatus.message)
            is StockStatusState.Loading -> {}
            is StockStatusState.Success -> {
                val picked = stockStatus.status.picked
                val followed = stockStatus.status.followed

                if (picked != null) {
                    DataSection(
                        title = "Your stock",
                        data = mapOf(
                            "Amount" to picked.amount.toString(),
                            "Buy price" to picked.spent.toCurrencyString(),
                            "Profit" to ((stock.price * picked.amount) - picked.spent).toCurrencyString()
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StockButton(
                        text = "PICK",
                        onClick = { navController.navigate(Screens.Pick.withArgs(stock.symbol)) })
                    Spacer(modifier = Modifier.height(8.dp))
                    StockButton(
                        text = "SELL",
                        onClick = {
                            navController.navigate(
                                Screens.Pick.withArgs(
                                    stock.symbol,
                                    true
                                )
                            )
                        })
                } else {
                    StockButton(
                        text = "PICK",
                        onClick = { navController.navigate(Screens.Pick.withArgs(stock.symbol)) })
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (followed != null) {
                    StockButton(text = "Remove from watchlist", onClick = onStockUnfollow)
                } else {
                    StockButton(text = "Add to watchlist", onClick = onStockFollow)
                }
            }
        }
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
                "Dividend yield" to (stock.dividendYield * 100).toPercentageString(),
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


@Composable
fun StockDetailsScreen(
    stockSymbol: String,
    stockDetailsViewModel: StockDetailsViewModel = viewModel(factory = StockDetailsViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val hasRequestedFetch = remember { mutableStateOf(false) }
    val stockDetailsState = stockDetailsViewModel.stockDetailsState
    val stockStatusState = stockDetailsViewModel.stockStatusState

    if (!hasRequestedFetch.value) {
        hasRequestedFetch.value = true
        stockDetailsViewModel.fetch(stockSymbol)
    }

    ThreeDotsLayout(stockSymbol) {
        when (stockDetailsState) {
            is StockDetailsState.Loading -> Loading()
            is StockDetailsState.Error -> Text(text = stockDetailsState.message)
            is StockDetailsState.Success -> {
                StockDetailsContent(
                    stock = stockDetailsState.stock,
                    stockStatus = stockStatusState,
                    onStockFollow = { stockDetailsViewModel.follow(stockSymbol) },
                    onStockUnfollow = { stockDetailsViewModel.unfollow(stockSymbol) },
                    modifier = modifier
                )
            }
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