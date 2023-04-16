package com.arthurdw.threedots.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.utils.PreviewWrapper

@Composable
fun ScanScreen() {
    ThreeDotsLayout("Scan") {

    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    PreviewWrapper {
        ScanScreen()
    }
}