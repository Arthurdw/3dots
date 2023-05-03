package com.arthurdw.threedots.ui.screens.news

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.components.Ripple
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.utils.toDateString
import java.lang.Float.min

@Composable
fun NewsItemRepresentation(
    item: NewsItem,
) {
    val painter = rememberAsyncImagePainter(item.imageUrl)
    val state = painter.state
    val context = LocalContext.current

    val transition by animateFloatAsState(
        targetValue = if (state !is AsyncImagePainter.State.Loading) 1f else 0f
    )

    @Composable
    fun ArticleDetails() {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.source,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.date.toDateString(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(100.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = transition),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .scale(.8f + (.2f * transition))
            .graphicsLayer { rotationX = (1f - transition) * 5f }
            .alpha(min(1f, transition / .2f))
            .clickable { context.startActivity(item.getShareIntent()) },
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            if (state !is AsyncImagePainter.State.Error) {
                Column {
                    if (state is AsyncImagePainter.State.Loading) {
                        Ripple(circleColor = MaterialTheme.colorScheme.primary, size = 100)
                    }
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        painter = painter,
                        contentDescription = stringResource(R.string.image_fitting_to_article),
                        contentScale = ContentScale.Crop,
                    )
                    ArticleDetails()
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.snippet,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .clip(RectangleShape)
                )
                if (state is AsyncImagePainter.State.Error) ArticleDetails()
            }
        }
    }
}

@Composable
fun News(
    uiState: NewsState.Success,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .verticalScroll(scrollState),
    ) {
        uiState.newsItems.forEach {
            Spacer(modifier = Modifier.height(12.dp))
            NewsItemRepresentation(it)
        }
    }
}

@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = viewModel(factory = NewsViewModel.Factory),
    modifier: Modifier = Modifier,
) {
    val state = newsViewModel.state

    ThreeDotsLayout(stringResource(R.string.news)) {
        when (state) {
            is NewsState.Success -> News(state)
            is NewsState.Error -> Text(text = state.message)
            is NewsState.Loading -> Loading()
        }
    }
}