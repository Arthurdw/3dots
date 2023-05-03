package com.arthurdw.threedots.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.QRScanner
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.utils.State


@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun ScanScreen() {
    val context = LocalContext.current
    val navController = State.LocalNavController.current

    val foundMessage = stringResource(R.string.qr_code_found)

    ThreeDotsLayout(stringResource(R.string.scan)) {
        QRScanner(
            onScan = {
                Toast.makeText(context, foundMessage, Toast.LENGTH_SHORT).show()
                navController.navigate(Screens.Overview.withArgs(it))
            }
        )
    }
}
