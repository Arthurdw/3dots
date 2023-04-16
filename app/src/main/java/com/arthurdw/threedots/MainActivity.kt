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
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme
import com.arthurdw.threedots.utils.LocalNavController

enum class Screens(val route: String) {
    SignInWith("sign_in_with"), Unlock("unlock"), Overview("overview"), News("news")
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
                            composable(Screens.SignInWith.route) {
                                SignInWith()
                            }
                            composable(Screens.Unlock.route) {
                                Unlock(text = "Welcome back, Arthur!", onSuccess = {
                                    navController.navigate(Screens.Overview.route)
                                })
                            }
                            composable(Screens.Overview.route) {
                                OverviewScreen()
                            }
                            composable(Screens.News.route) {
                                NewsScreen()
                            }
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