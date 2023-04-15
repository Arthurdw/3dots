package com.arthurdw.threedots

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme
import com.google.android.gms.common.SignInButton

@Composable
fun SignInWith() {
    val icon = painterResource(id = R.drawable.ic_launcher_foreground)

    ThreeDotsTheme {
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
                        modifier = Modifier.size(350.dp)
                    )
                }

                Surface(modifier = Modifier.padding(bottom = 100.dp)) {
                    AndroidView(factory = { context ->
                        SignInButton(context).apply {
                            setSize(SignInButton.SIZE_WIDE)
                            setStyle(SignInButton.COLOR_LIGHT, SignInButton.SIZE_WIDE)
                        }
                    })

                }
            }
        }
    }
}

