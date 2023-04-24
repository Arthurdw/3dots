package com.arthurdw.threedots.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arthurdw.threedots.ui.screens.NewsScreen
import com.arthurdw.threedots.ui.screens.OverviewScreen
import com.arthurdw.threedots.ui.screens.PickScreen
import com.arthurdw.threedots.ui.screens.ScanScreen
import com.arthurdw.threedots.ui.screens.SettingsScreen
import com.arthurdw.threedots.ui.screens.ShareScreen
import com.arthurdw.threedots.ui.screens.SignInWith
import com.arthurdw.threedots.ui.screens.StockDetailsScreen
import com.arthurdw.threedots.ui.screens.StocksScreen
import com.arthurdw.threedots.ui.screens.Unlock
import com.arthurdw.threedots.utils.LocalNavController

enum class Screens(val route: String) {
    News("news"),
    Overview("overview"),
    Pick("pick"),
    Scan("scan"),
    Settings("settings"),
    Share("share"),
    SignInWith("sign_in_with"),
    StockDetails("stock"),
    Stocks("stocks"),
    Unlock("unlock");

    fun <T: Any>withArgs(vararg args: T): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

@Composable
fun ThreeDotsApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

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
                composable(Screens.Overview.route) { OverviewScreen(null) }
                composable(
                    Screens.Overview.route + "/{id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.StringType
                    })
                ) {
                    val id = it.arguments?.getString("id")
                    OverviewScreen(id)
                }
                composable(Screens.News.route) { NewsScreen() }
                composable(Screens.Stocks.route) { StocksScreen() }
                composable(
                    Screens.StockDetails.route + "/{symbol}",
                    arguments = listOf(navArgument("symbol") {
                        type = NavType.StringType
                    })
                ) {
                    val symbol = it.arguments?.getString("symbol")

                    // A symbol is required to show the details screen.
                    if (symbol == null) {
                        Log.e("MainActivity", "StockDetailsScreen: symbol is null")
                        return@composable
                    }

                    StockDetailsScreen(symbol)
                }
                composable(Screens.Share.route) { ShareScreen() }
                composable(Screens.Settings.route) { SettingsScreen() }
                composable(Screens.Scan.route) { ScanScreen() }
                composable(
                    Screens.Pick.route + "/{stockId}",
                    arguments = listOf(navArgument("stockId") {
                        type = NavType.StringType
                    })
                ) {
                    val stockId = it.arguments?.getString("stockId")

                    if (stockId == null) {
                        Log.e("MainActivity", "PickScreen: stockId is null")
                        return@composable
                    }

                    PickScreen(stockId)
                }
                composable(
                    Screens.Pick.route + "/{stockId}/{sell}",
                    arguments = listOf(
                        navArgument("stockId") {
                            type = NavType.StringType
                        },
                        navArgument("sell") {
                            type = NavType.BoolType
                        }
                    )
                ) {
                    val stockId = it.arguments?.getString("stockId")
                    val sell = it.arguments?.getBoolean("sell")

                    if (stockId == null || sell == null) {
                        Log.e("MainActivity", "PickScreen: stockId or sell is null")
                        return@composable
                    }

                    PickScreen(stockId, sell)
                }
            }
        }
    }
}