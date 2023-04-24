package com.arthurdw.threedots.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.QRScanner
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.utils.LocalNavController


@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun ScanScreen() {
    val context = LocalContext.current
    val navController = LocalNavController.current

    ThreeDotsLayout("Scan") {
        QRScanner(
            onScan = {
                Toast.makeText(context, "QR code found...", Toast.LENGTH_SHORT).show()
                navController.navigate(Screens.Overview.withArgs(it))
            }
        )
    }
}
