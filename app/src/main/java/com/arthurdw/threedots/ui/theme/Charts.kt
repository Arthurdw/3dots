package com.arthurdw.threedots.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders

@Composable
internal fun rememberChartStyle(): ChartStyle {
    return remember {
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = MyrtleGreen,
                axisGuidelineColor = MyrtleGreen,
                axisLineColor = MyrtleGreen,
            ), ChartStyle.ColumnChart(
                columns = listOf(),
            ), ChartStyle.LineChart(
                listOf(
                    LineChart.LineSpec(
                        lineColor = EtonBlue.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    EtonBlue.copy(alpha = 0.5f),
                                    EtonBlue.copy(alpha = 0.1f),
                                ),
                            ),
                        ),
                    )
                ),
            ), ChartStyle.Marker(), EtonBlue
        )
    }
}