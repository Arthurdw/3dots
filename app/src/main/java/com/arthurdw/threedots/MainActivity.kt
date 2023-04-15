package com.arthurdw.threedots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme

enum class Screens(val route: String) {
    SignInWith("sign_in_with"), Unlock("unlock"), Overview("overview")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            ThreeDotsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = Screens.SignInWith.route) {
                        composable(Screens.SignInWith.route) {
                            SignInWith(onSignIn = {
                                navController.navigate(Screens.Unlock.route)
                            })
                        }
                        composable(Screens.Unlock.route) {
                            Unlock(text = "Welcome back, Arthur!", onSuccess = {
                                navController.navigate(Screens.Overview.route)
                            })
                        }
                        composable(Screens.Overview.route) {
                            OverviewScreen(navController)
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
        SignInWith {}
    }
}