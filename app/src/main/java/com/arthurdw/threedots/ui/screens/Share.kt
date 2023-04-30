package com.arthurdw.threedots.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.data.objects.ShareUser
import com.arthurdw.threedots.ui.screens.overview.FollowedStocksState
import com.arthurdw.threedots.ui.screens.overview.OverviewViewModel
import com.arthurdw.threedots.ui.screens.overview.PickedStocksState
import com.arthurdw.threedots.ui.screens.overview.WorthState
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.serialization.json.Json

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
fun ShareScreen(
    modifier: Modifier = Modifier,
    overviewViewModel: OverviewViewModel = viewModel(factory = OverviewViewModel.Factory)
) {
    val user by remember { derivedStateOf { State.LocalUser } }
    val worthState = overviewViewModel.worthState
    val pickedStocksState = overviewViewModel.pickedStocksState
    val followedStocksState = overviewViewModel.followedStocksState

    ThreeDotsLayout("Share") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = user.username,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = 16.dp),
            )
            when {
                worthState is WorthState.Success
                        && pickedStocksState is PickedStocksState.Success
                        && followedStocksState is FollowedStocksState.Success -> {
                    val shareUser = ShareUser(
                        user = user,
                        worth = worthState.value,
                        pickedStocks = pickedStocksState.value,
                        followedStocks = followedStocksState.value,
                    )
                    QRCode(Json.encodeToString(ShareUser.serializer(), shareUser))
                }
                worthState is WorthState.Error -> Text(worthState.message)
                pickedStocksState is PickedStocksState.Error -> Text(pickedStocksState.message)
                followedStocksState is FollowedStocksState.Error -> Text(followedStocksState.message)
                else -> Loading()
            }
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
