package com.arthurdw.threedots.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arthurdw.threedots.R
import com.arthurdw.threedots.Screens
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

private fun createGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}

@Composable
fun GoogleSignInButton(onSignIn: (GoogleSignInAccount) -> Unit) {
    val signInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                onSignIn(account!!)
            } catch (e: ApiException) {
                Log.e("3dots", "Failed to sign in with Google: ${e.message}")
            }
        }

    AndroidView(factory = { context ->
        SignInButton(context).apply {
            setSize(SignInButton.SIZE_WIDE)
            setStyle(SignInButton.COLOR_LIGHT, SignInButton.SIZE_WIDE)
        }
    }) { signInButton ->
        signInButton.setOnClickListener {
            signInLauncher.launch(googleSignInClient.signInIntent)
        }
    }
}

@Composable
fun SignInWith() {
    val context = LocalContext.current as Activity
    val icon = painterResource(id = R.drawable.ic_launcher_foreground)
    val navController = LocalNavController.current

    googleSignInClient = createGoogleSignInClient(context)

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
                GoogleSignInButton(onSignIn = {
                    // TODO: Complete sign up/sign in
                    // TODO: Share the user like the localnavcontroller
                    Log.d("3dots", "SignInWith: $it")
                    Log.d("3dots", "SignInWith: ${it.email} ${it.givenName}")
                    navController.navigate(Screens.Overview.route)
                })
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

