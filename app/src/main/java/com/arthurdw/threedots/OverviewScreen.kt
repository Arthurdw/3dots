package com.arthurdw.threedots

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme
import com.arthurdw.threedots.ui.theme.rememberChartStyle
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun OverviewScreen() {
    val worth = entryModelOf(
        10000.0f, 8000.0f, 11000.0f, 13250.0f, 10500.0f, 11750.0f, 14000.0f, 12250.0f
    )

    ThreeDotsLayout("Arthurdw") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProvideChartStyle(rememberChartStyle()) {
                Chart(
                    chart = lineChart(),
                    model = worth,
                    startAxis = startAxis(),
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
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