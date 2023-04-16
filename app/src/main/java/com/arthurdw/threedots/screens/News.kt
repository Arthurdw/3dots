package com.arthurdw.threedots.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme

@Composable
fun NewsScreen() {
    ThreeDotsLayout("News") {

    }
}

@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    ThreeDotsTheme {
        NewsScreen()
    }
}