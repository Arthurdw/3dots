package com.arthurdw.threedots.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Stock
import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.ui.theme.rememberChartStyle
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.toCurrencyString
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entryModelOf

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
fun OverviewScreen() {
    val worth = entryModelOf(
        100000f, 80000f, 110000f, 132500f, 105000f, 117500f, 140000f, 122500f
    )

    val pickedStocks = listOf(
        BasicStock("Alphabet", "GOOGL", 15 * 93.65f, 15 * 86.7f),
        BasicStock("Apple", "AAPL", 15 * 168.56f, 15 * 121.4f),
    )

    val followedStocks = listOf(
        BasicStock("Alphabet", "GOOGL", 93.65f, 101.7f),
        BasicStock("Amazon", "AMZN", 331.48f, 309.04f),
        BasicStock("Apple", "AAPL", 168.56f, 161.4f),
        BasicStock("Facebook", "FB", 311.54f, 304.67f),
        BasicStock("Johnson & Johnson", "JNJ", 165.55f, 165.22f),
        BasicStock("Microsoft", "MSFT", 264.11f, 266.71f),
        BasicStock("Tesla", "TSLA", 710.44f, 699.68f)
    )

    val scrollState = rememberScrollState()

    ThreeDotsLayout("Arthurdw") {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .height(200.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProvideChartStyle(rememberChartStyle()) {
                Chart(
                    chart = lineChart(),
                    model = worth,
                    startAxis = startAxis(),
                    modifier = Modifier.fillMaxWidth(0.9f),
                )
            }

            BalanceStatistic("Spent:", 100_000f, modifier = Modifier.padding(top = 20.dp))
            BalanceStatistic("Gained:", 22_500f)

            StocksSection("Picked stocks", pickedStocks, modifier = Modifier.padding(top = 20.dp))
            StocksSection(
                "Followed stocks",
                followedStocks,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    PreviewWrapper {
        OverviewScreen()
    }
}