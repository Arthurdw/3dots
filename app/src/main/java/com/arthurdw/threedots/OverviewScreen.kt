package com.arthurdw.threedots

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme
import com.arthurdw.threedots.ui.theme.rememberChartStyle
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
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value.toCurrencyString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun OverviewScreen() {
    val worth = entryModelOf(
        100000f, 80000f, 110000f, 132500f, 105000f, 117500f, 140000f, 122500f
    )

    ThreeDotsLayout("Arthurdw") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    ThreeDotsTheme {
        OverviewScreen()
    }
}