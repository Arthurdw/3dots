package com.arthurdw.threedots

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ThreeDotsLayout(content: @Composable () -> Unit) {
    content()
}

@Preview(showBackground = true)
@Composable
fun ThreeDotsLayoutPreview() {
    ThreeDotsLayout {
        Text(text = "Hello World!")
    }
}