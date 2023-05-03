package com.arthurdw.threedots.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.data.objects.ShareUser
import com.arthurdw.threedots.ui.screens.overview.FollowedStocksState
import com.arthurdw.threedots.ui.screens.overview.OverviewViewModel
import com.arthurdw.threedots.ui.screens.overview.PickedStocksState
import com.arthurdw.threedots.ui.screens.overview.WorthState
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.utils.generateQRCode
import kotlinx.serialization.json.Json


@Composable
fun QRCode(content: String) {
    val qrCodeBitmap = generateQRCode(content)
    Image(
        bitmap = qrCodeBitmap,
        contentDescription = stringResource(R.string.user_id_is) + content,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
    )
}


@Composable
fun ShareScreen(
    modifier: Modifier = Modifier,
    overviewViewModel: OverviewViewModel = viewModel(factory = OverviewViewModel.Factory)
) {
    val user by remember { derivedStateOf { State.CurrentUser } }
    val worthState = overviewViewModel.worthState
    val pickedStocksState = overviewViewModel.pickedStocksState
    val followedStocksState = overviewViewModel.followedStocksState

    ThreeDotsLayout(stringResource(R.string.share)) {
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
