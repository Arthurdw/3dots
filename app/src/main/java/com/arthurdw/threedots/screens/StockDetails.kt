package com.arthurdw.threedots.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.utils.PreviewWrapper

@Composable
fun StockDetailsScreen(stockSymbol: String) {
    ThreeDotsLayout(stockSymbol) {

    }
}

@Preview(showBackground = true)
@Composable
fun StockDetailsScreenPreview() {
    PreviewWrapper {
        StockDetailsScreen("AAPL")
    }
}