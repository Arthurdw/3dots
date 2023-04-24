package com.arthurdw.threedots.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.arthurdw.threedots.R
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.utils.LocalNavController
import com.arthurdw.threedots.utils.PreviewWrapper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

@SuppressLint("StaticFieldLeak")
private lateinit var googleSignInClient: GoogleSignInClient

var isFirstRun = true

private fun createGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()


    return GoogleSignIn.getClient(context, gso)
}

@Composable
fun GoogleSignInButton(
    onSignIn: (GoogleSignInAccount) -> Unit,
    onClick: () -> Unit = {},
    onFail: () -> Unit = {}
) {
    val signInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                onSignIn(account!!)
            } catch (e: ApiException) {
                Log.e("3dots", "Failed to sign in with Google: ${e.message}")
                onFail()
            }
        }

    AndroidView(factory = { context ->
        SignInButton(context).apply {
            setSize(SignInButton.SIZE_WIDE)
            setStyle(SignInButton.COLOR_LIGHT, SignInButton.SIZE_WIDE)
        }
    }) { signInButton ->
        signInButton.setOnClickListener {
            onClick()
            signInLauncher.launch(googleSignInClient.signInIntent)
        }
    }
}

@Composable
fun SignInWith() {
    val context = LocalContext.current as Activity
    val icon = painterResource(id = R.drawable.ic_launcher_foreground)
    val navController = LocalNavController.current
    val isLoading = remember { mutableStateOf(false) }

    googleSignInClient = remember { createGoogleSignInClient(context) }

    val lastSignInAccount = remember(key1 = isFirstRun) {
        if (isFirstRun) GoogleSignIn.getLastSignedInAccount(context)
        else null // Return null on subsequent calls
    }
    val isDoneProcessing = lastSignInAccount == null && !isFirstRun
    isFirstRun = false

    if (lastSignInAccount != null) {
        // TODO: Set state
        Log.d(
            "3dots",
            "LastSignInAccount: ${lastSignInAccount.email} ${lastSignInAccount.givenName}"
        )
        Log.d(
            "3dots",
            "SignInWith: ${lastSignInAccount.idToken}"
        )
//        Api.public.loginOrRegisterUser(LoginData(lastSignInAccount.idToken!!))
        navController.navigate(Screens.Overview.route)
        googleSignInClient.signOut()
    }

    Log.d("3dots", "SignInWith: $isDoneProcessing $isLoading")

    if (!isDoneProcessing || isLoading.value) {
        Loading(
            // Small trick to prevent blocking behaviour
            Modifier
                .zIndex(9999999f)
                .background(color = MaterialTheme.colorScheme.background)
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.padding(bottom = 25.dp)
            ) {
                Image(
                    painter = icon,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(350.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                )
            }

            Surface(
                modifier = Modifier.padding(bottom = 100.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                GoogleSignInButton(
                    onSignIn = {
                        // TODO: Complete sign up/sign in
                        // TODO: Share the user like the localnavcontroller
                        Log.d("3dots", "SignInWith: $it")
                        Log.d("3dots", "SignInWith: ${it.email} ${it.givenName}")
                        Log.d("3dots", "SignInWith: ${it.idToken}")
                        navController.navigate(Screens.Overview.route)
                    },
                    onClick = {
                        isLoading.value = true
                        Log.d("3dots", "SignInWith: Loading...")
                    },
                    onFail = {
                        isLoading.value = false
                        Log.d("3dots", "SignInWith: Failed to load")
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInWithPreview() {
    PreviewWrapper {
        SignInWith()
    }
}

