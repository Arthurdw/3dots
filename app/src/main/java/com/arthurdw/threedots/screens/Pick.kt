package com.arthurdw.threedots.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.utils.PreviewWrapper

@Composable
fun PickScreen(stockId: String, sell: Boolean = false) {
    val title = if (sell) "Sell" else "Buy"

    ThreeDotsLayout(title) {

    }
}

@Preview(showBackground = true)
@Composable
fun PickScreenPreview() {
    PreviewWrapper {
        PickScreen("APL")
    }
}
