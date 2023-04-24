package com.arthurdw.threedots.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.utils.PreviewWrapper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

private fun generateQRCode(content: String): ImageBitmap {
    val writer = QRCodeWriter()
    try {
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) Color.Black.toArgb() else Color.White.toArgb()
            }
        }
        val bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
        return bitmap.asImageBitmap()
    } catch (e: WriterException) {
        // TODO: Handle exception
        return ImageBitmap(1, 1)
    }
}

@Composable
fun QRCode(content: String) {
    val qrCodeBitmap = generateQRCode(content)
    Image(
        bitmap = qrCodeBitmap,
        contentDescription = "User id is: $content",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
    )
}


@Composable
fun ShareScreen() {
    ThreeDotsLayout("Share") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO: Get data dynamically
            Text(
                text = "Arthurdw",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = 16.dp),
            )
            QRCode("6e8b6d3a-3b7d-11eb-adc1-0242ac120002")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShareScreenPreview() {
    PreviewWrapper {
        ShareScreen()
    }
}
