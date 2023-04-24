package com.arthurdw.threedots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arthurdw.threedots.ui.ThreeDotsApp
import com.arthurdw.threedots.ui.screens.SignInWith
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ThreeDotsTheme {
                ThreeDotsApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ThreeDotsTheme {
        SignInWith()
    }
}