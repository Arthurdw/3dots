package com.arthurdw.threedots.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.ui.theme.rememberChartStyle
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.utils.empty
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
    val navController = State.LocalNavController.current

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
                        minY = minimumValue, maxY = maximumValue
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
            is StockStatusState.Error -> {
                Text(
                    stringResource(R.string.failed_get_to_your_personal_data), modifier = Modifier.padding(bottom = 12.dp)
                )
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(text = stringResource(R.string.retry))
                }
            }

            is StockStatusState.Success -> {
                val picked = stockStatus.status.picked
                val followed = stockStatus.status.followed

                if (picked != null) {
                    DataSection(
                        title = stringResource(R.string.your_stock), data = mapOf(
                            stringResource(R.string.amount) to picked.amount.toString(),
                            stringResource(R.string.buy_price) to picked.spent.toCurrencyString(),
                            stringResource(R.string.profit) to ((stock.price * picked.amount) - picked.spent).toCurrencyString()
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StockButton(text = stringResource(R.string.pick),
                        onClick = { navController.navigate(Screens.Pick.withArgs(stock.symbol)) })
                    Spacer(modifier = Modifier.height(8.dp))
                    StockButton(text = stringResource(R.string.sell), onClick = {
                        navController.navigate(
                            Screens.Pick.withArgs(
                                stock.symbol, true
                            )
                        )
                    })
                } else {
                    StockButton(text = stringResource(R.string.pick),
                        onClick = { navController.navigate(Screens.Pick.withArgs(stock.symbol)) })
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (followed != null) {
                    StockButton(text = stringResource(R.string.remove_from_watchlist), onClick = onStockUnfollow)
                } else {
                    StockButton(text = stringResource(R.string.add_to_watchlist), onClick = onStockFollow)
                }
            }

            else -> {}
        }
        Spacer(modifier = Modifier.height(8.dp))
        DataSection(
            title = stringResource(R.string.general), data = mapOf(
                stringResource(R.string.bid) to stock.bid.toCurrencyString(),
                stringResource(R.string.ask) to stock.ask.toCurrencyString(),
                stringResource(R.string.open) to (stock.open?.toCurrencyString() ?: stringResource(R.string.not_available)),
                stringResource(R.string.previous_close) to stock.close.toCurrencyString(),
                stringResource(R.string.today_s_range) to "${minimumValue.toCurrencyString()} - ${maximumValue.toCurrencyString()}",
                stringResource(R.string.fiftytwo_week_range) to "${stock.fiftyTwoWeekLow.toCurrencyString()} - ${stock.fiftyTwoWeekHigh.toCurrencyString()}",
                stringResource(R.string.analyst_target_price) to (stock.analystTargetPrice?.toCurrencyString() ?: stringResource(R.string.not_available)),
                stringResource(R.string.market_cap) to stock.marketCapitalization.toCurrencyString(),
                stringResource(R.string.revenue) to stock.revenueTTM.toCurrencyString(),
                stringResource(R.string.gross_profit) to stock.grossProfitTTM.toCurrencyString(),
                stringResource(R.string.ebitda) to stock.ebitda.toCurrencyString(),
                stringResource(R.string.p_e_ratio) to stock.pERatio,
                stringResource(R.string.p_e_g_ratio) to stock.pEGRatio,
                stringResource(R.string.eps) to stock.ePS,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        DataSection(
            title = stringResource(R.string.dividend), data = mapOf(
                stringResource(R.string.dividend_yield) to (stock.dividendYield * 100).toPercentageString(),
                stringResource(R.string.dividend_p_s) to stock.dividendPerShare.toCurrencyString(),
                stringResource(R.string.ex_dividend_date) to stock.exDividendDate.toDateString(),
                stringResource(R.string.dividend_date) to stock.dividendDate.toDateString(),
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        DataSection(
            title = stringResource(R.string.about), data = mapOf(
                stringResource(R.string.symbol) to stock.symbol,
                stringResource(R.string.asset_type) to stock.assetType,
                stringResource(R.string.name) to stock.name,
                stringResource(R.string.cik) to stock.cIK,
                stringResource(R.string.exchange) to stock.exchange,
                stringResource(R.string.currency) to stock.currency,
                stringResource(R.string.country) to stock.country,
                stringResource(R.string.sector) to stock.sector,
                stringResource(R.string.industry) to stock.industry,
                stringResource(R.string.address) to stock.address,
                stringResource(R.string.fiscal_year_end) to stock.fiscalYearEnd,
                stringResource(R.string.latest_quarter) to stock.latestQuarter.toDateString(),
                stringResource(R.string.description) to String.empty()
            )
        )
        Text(
            text = stock.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DataSection(
            title = stringResource(R.string.other_details), data = mapOf(
                stringResource(R.string.fifty_day_moving_average) to stock.fiftyDayMovingAverage.toCurrencyString(),
                stringResource(R.string.two_hundred_day_moving_average) to stock.twoHundredDayMovingAverage.toCurrencyString(),
                stringResource(R.string.shares_outstanding) to stock.sharesOutstanding.toCurrencyString(),
                stringResource(R.string.book_value) to stock.bookValue.toCurrencyString(),
                stringResource(R.string.revenue_per_share_ttm) to stock.revenuePerShareTTM.toCurrencyString(),
                stringResource(R.string.profit_margin) to stock.profitMargin.toPercentageString(),
                stringResource(R.string.operating_margin_ttm) to stock.operatingMarginTTM.toPercentageString(),
                stringResource(R.string.return_on_assets_ttm) to stock.returnOnAssetsTTM.toPercentageString(),
                stringResource(R.string.return_on_equity_ttm) to stock.returnOnEquityTTM.toPercentageString(),
                stringResource(R.string.diluted_eps_ttm) to stock.dilutedEPSTTM.toCurrencyString(),
                stringResource(R.string.quarterly_earnings_growth_yoy) to stock.quarterlyEarningsGrowthYOY.toPercentageString(),
                stringResource(R.string.quarterly_revenue_growth_yoy) to stock.quarterlyRevenueGrowthYOY.toPercentageString(),
                stringResource(R.string.trailing_pe) to stock.trailingPE,
                stringResource(R.string.forward_pe) to stock.forwardPE,
                stringResource(R.string.price_to_sales_ratio_ttm) to stock.priceToSalesRatioTTM,
                stringResource(R.string.price_to_book_ratio) to stock.priceToBookRatio,
                stringResource(R.string.ev_to_revenue) to stock.eVToRevenue,
                stringResource(R.string.ev_to_ebitda) to stock.eVToEBITDA,
                stringResource(R.string.beta) to stock.beta,
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
            is StockDetailsState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.failed_to_fetch_stock_details), modifier = Modifier.padding(bottom = 12.dp)
                    )
                    TextButton(onClick = { stockDetailsViewModel.fetch(stockSymbol) }) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

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