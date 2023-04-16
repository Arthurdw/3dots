package com.arthurdw.threedots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arthurdw.threedots.screens.PickScreen
import com.arthurdw.threedots.screens.ScanScreen
import com.arthurdw.threedots.screens.SettingsScreen
import com.arthurdw.threedots.screens.ShareScreen
import com.arthurdw.threedots.screens.StockDetailsScreen
import com.arthurdw.threedots.screens.StocksScreen
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme
import com.arthurdw.threedots.utils.LocalNavController

enum class Screens(val route: String) {
    SignInWith("sign_in_with"),
    Unlock("unlock"),
    Overview("overview"),
    News("news"),
    Stocks("stocks"),
    StockDetails("stock_details"),
    Share("share"),
    Settings("settings"),
    Scan("scan"),
    Pick("pick"),
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            ThreeDotsTheme {
                CompositionLocalProvider(LocalNavController provides navController) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController, startDestination = Screens.SignInWith.route) {
                            composable(Screens.SignInWith.route) { SignInWith() }
                            composable(Screens.Unlock.route) {
                                Unlock(text = "Welcome back, Arthur!", onSuccess = {
                                    navController.navigate(Screens.Overview.route)
                                })
                            }
                            composable(Screens.Overview.route) { OverviewScreen() }
                            composable(Screens.News.route) { NewsScreen() }
                            composable(Screens.Stocks.route) { StocksScreen() }
                            composable(Screens.StockDetails.route) { StockDetailsScreen("AAPL") }
                            composable(Screens.Share.route) { ShareScreen() }
                            composable(Screens.Settings.route) { SettingsScreen() }
                            composable(Screens.Scan.route) { ScanScreen() }
                            composable(Screens.Pick.route) { PickScreen() }
                        }
                    }
                }
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