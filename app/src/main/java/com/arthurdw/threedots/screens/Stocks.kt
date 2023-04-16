package com.arthurdw.threedots.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme
import com.arthurdw.threedots.utils.PreviewWrapper

@Composable
fun StocksScreen() {
    ThreeDotsLayout("Stocks") {

    }
}

@Preview(showBackground = true)
@Composable
fun StocksScreenPreview() {
    PreviewWrapper {
        StocksScreen()
    }
}