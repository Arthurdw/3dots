package com.arthurdw.threedots.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.QRScanner


@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun ScanScreen() {
    val context = LocalContext.current

    ThreeDotsLayout("Scan") {
        QRScanner(
            onScan = {
                Toast.makeText(context, "Barcode found", Toast.LENGTH_SHORT).show()

            }
        )
    }
}
