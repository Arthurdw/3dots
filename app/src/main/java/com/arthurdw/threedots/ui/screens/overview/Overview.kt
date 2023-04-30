package com.arthurdw.threedots.ui.screens.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.components.Stock
import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.data.objects.ShareUser
import com.arthurdw.threedots.data.objects.User
import com.arthurdw.threedots.data.objects.toBasicStocks
import com.arthurdw.threedots.ui.theme.rememberChartStyle
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.utils.toCurrencyString
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.serialization.json.Json

@Composable
fun BalanceStatistic(title: String, value: Float, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title, style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = value.toCurrencyString(), style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun StocksSection(title: String, data: List<BasicStock>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(0.9f)) {
        Text(
            text = title, style = MaterialTheme.typography.headlineSmall
        )
        data.forEach {
            Stock(it, modifier = Modifier.padding(top = 12.dp))
        }
    }
}

@Composable
fun OverviewScreenContent(
    user: User,
    worthState: WorthState,
    pickedStocksState: PickedStocksState,
    followedStocksState: FollowedStocksState,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    ThreeDotsLayout(user.username) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .height(200.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (worthState) {
                is WorthState.Loading -> Loading()
                is WorthState.Error -> Text(text = "Error")
                is WorthState.Success -> {
                    val worth = worthState.value
                    if (worth.isNotEmpty()) {
                        ProvideChartStyle(rememberChartStyle()) {
                            Chart(
                                chart = lineChart(
                                    axisValuesOverrider = AxisValuesOverrider.fixed(
                                        minY = worth.min(),
                                        maxY = worth.max()
                                    )
                                ),
                                model = entryModelOf(*worth.toTypedArray()),
                                endAxis = endAxis(),
                                modifier = Modifier.fillMaxWidth(0.9f),
                                chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false)
                            )
                        }
                    }
                }
            }

            BalanceStatistic("Spent:", user.spent, modifier = Modifier.padding(top = 20.dp))
            BalanceStatistic("Gained:", user.gained)

            when (pickedStocksState) {
                is PickedStocksState.Loading -> Loading()
                is PickedStocksState.Error -> Text(text = "Error")
                is PickedStocksState.Success ->
                    StocksSection(
                        "Picked stocks",
                        pickedStocksState.value.toBasicStocks(),
                        modifier = Modifier.padding(top = 20.dp)
                    )
            }

            when (followedStocksState) {
                is FollowedStocksState.Loading -> Loading()
                is FollowedStocksState.Error -> Text(text = "Error")
                is FollowedStocksState.Success ->
                    StocksSection(
                        "Watchlist",
                        followedStocksState.value,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                    )
            }
        }
    }
}

@Composable
fun OverviewScreenCurrentUser(
    overviewViewModel: OverviewViewModel = viewModel(factory = OverviewViewModel.Factory),
    modifier: Modifier = Modifier,
) {
    val user by remember { derivedStateOf { State.LocalUser } }

    val worthState = overviewViewModel.worthState
    val pickedStocksState = overviewViewModel.pickedStocksState
    val followedStocksState = overviewViewModel.followedStocksState

    OverviewScreenContent(
        user = user,
        worthState = worthState,
        pickedStocksState = pickedStocksState,
        followedStocksState = followedStocksState,
        modifier = modifier,
    )
}

@Composable
fun OverviewScreen(
    shareUserSerialized: String? = null,
    modifier: Modifier = Modifier,
) {
    if (shareUserSerialized != null) {
        val shareUser = Json.decodeFromString(ShareUser.serializer(), shareUserSerialized)
        OverviewScreenContent(
            user = shareUser.user,
            worthState = WorthState.Success(shareUser.worth),
            pickedStocksState = PickedStocksState.Success(shareUser.pickedStocks),
            followedStocksState = FollowedStocksState.Success(shareUser.followedStocks),
            modifier = modifier,
        )
    } else {
        OverviewScreenCurrentUser()
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    PreviewWrapper {
        OverviewScreen(null)
    }
}