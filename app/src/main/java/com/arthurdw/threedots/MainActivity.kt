package com.arthurdw.threedots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme

enum class Screens(val route: String) {
    SignInWith("sign_in_with"), Unlock("unlock")
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
                    NavHost(navController, startDestination = Screens.SignInWith.name) {
                        composable(Screens.SignInWith.name) {
                            SignInWith(onSignIn = {
                                navController.navigate(Screens.Unlock.name)
                            })
                        }
                        composable(Screens.Unlock.name) {
                            Unlock(text = "Welcome back, Arthur!")
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