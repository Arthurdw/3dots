package com.arthurdw.threedots

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme

@Composable
fun OverviewScreen() {
    ThreeDotsLayout("Arthurdw") {
        Text(text = "Overview")
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    ThreeDotsTheme {
        OverviewScreen()
    }
}