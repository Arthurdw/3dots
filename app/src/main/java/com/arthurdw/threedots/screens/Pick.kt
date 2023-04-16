package com.arthurdw.threedots.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.utils.PreviewWrapper

@Composable
fun PickScreen() {
    ThreeDotsLayout("Pick") {

    }
}

@Preview(showBackground = true)
@Composable
fun PickScreenPreview() {
    PreviewWrapper {
        PickScreen()
    }
}
