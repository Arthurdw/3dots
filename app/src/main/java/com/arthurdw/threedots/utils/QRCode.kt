package com.arthurdw.threedots.utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

/**
 * Generates a QR code [ImageBitmap] for the given content.
 *
 * @param content the content to be encoded in the QR code
 * @return the generated QR code as an [ImageBitmap]
 */
fun generateQRCode(content: String): ImageBitmap {
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
        return ImageBitmap(1, 1)
    }
}
