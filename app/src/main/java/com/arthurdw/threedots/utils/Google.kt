package com.arthurdw.threedots.utils

import android.content.Context
import com.arthurdw.threedots.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


/**
 * Creates a Google Sign-In client with the given [Context].
 * @param context the context used to create the client
 * @return the created [GoogleSignInClient] object
 */
fun createGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()


    return GoogleSignIn.getClient(context, gso)
}
