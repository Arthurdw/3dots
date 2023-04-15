package com.arthurdw.threedots

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun OverviewScreen(navController: NavController) {
    ThreeDotsLayout {
        Text(text = "Overview")
    }
}