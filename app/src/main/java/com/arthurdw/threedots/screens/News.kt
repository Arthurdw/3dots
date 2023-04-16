package com.arthurdw.threedots.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Ripple
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.utils.PreviewWrapper

@Composable
fun NewsItemRepresentation(
    item: NewsItem,
) {
    val painter = rememberAsyncImagePainter(item.imageUrl)
    val state = painter.state

    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f
    )

    Log.d("3dot", "NewsItemRepresentation: ${painter.state is AsyncImagePainter.State.Loading}")
    Log.d("3dot", "NewsItemRepresentation 2: ${painter.state is AsyncImagePainter.State.Success}")
    Log.d("3dot", "NewsItemRepresentation 3: ${painter.state}")

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(16.dp),
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
                            .clip(RectangleShape),
                        painter = painter,
                        contentDescription = "Image fitting to article",
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.source,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .width(100.dp)
                            .clip(RectangleShape)
                    )
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
                if (state is AsyncImagePainter.State.Error) {
                    Text(
                        text = item.source,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .width(100.dp)
                            .clip(RectangleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun NewsScreen() {
    val scrollState = rememberScrollState()

    val news = listOf(
        NewsItem(
            title = "Top 7 valued firms together add ₹ 67,859.77 cr in Mcap; check list here",
            snippet = "A total of seven firms, out of the top 10 most valuable companies, together added ₹67,859.77 crore in market valuation in a holiday-shortened last week, with ...",
            url = "https://www.livemint.com/news/india/top-7-valued-firms-together-add-rs-67-859-77-cr-in-mcap-check-list-here-11681623243289.html",
            imageUrl = "https://www.livemint.com/lm-img/img/2023/04/16/600x338/ICICI_Bank_1681624374093_1681624374296_1681624374296.jpg",
            source = "livemint.com",
            dateStr = "2023-04-16T05:56:31.000000Z"
        ),
        NewsItem(
            title = "Mcap of seven of top-10 most valued firms climb Rs 67,859.77 cr; ICICI Bank, HDFC Bank shine",
            snippet = "Seven of the top-10 most valued firms together added Rs 67,859.77 crore in market valuation in a holiday-shortened last week, with ICICI Bank and HDFC Bank emer...",
            url = "https://economictimes.indiatimes.com/markets/stocks/news/mcap-of-seven-of-top-10-most-valued-firms-climb-rs-67859-77-cr-icici-bank-hdfc-bank-shine/articleshow/99529850.cms",
            imageUrl = "https://img.etimg.com/thumb/msid-99529868,width-1070,height-580,imgsize-32610,overlay-etmarkets/photo.jpg",
            source = "economictimes.indiatimes.com",
            dateStr = "2023-04-16T05:17:59.000000Z"
        ),
        NewsItem(
            title = "Top 7 valued firms together add ₹ 67,859.77 cr in Mcap; check list here",
            snippet = "A total of seven firms, out of the top 10 most valuable companies, together added ₹67,859.77 crore in market valuation in a holiday-shortened last week, with ...",
            url = "https://www.livemint.com/news/india/top-7-valued-firms-together-add-rs-67-859-77-cr-in-mcap-check-list-here-11681623243289.html",
            imageUrl = "https://www.livemint.com/lm-img/img/2023/04/16/600x338/ICICI_Bank_1681624374093_1681624374296_1681624374296.jpg",
            source = "livemint.com",
            dateStr = "2023-04-16T05:56:31.000000Z"
        ),
        NewsItem(
            title = "Mcap of seven of top-10 most valued firms climb Rs 67,859.77 cr; ICICI Bank, HDFC Bank shine",
            snippet = "Seven of the top-10 most valued firms together added Rs 67,859.77 crore in market valuation in a holiday-shortened last week, with ICICI Bank and HDFC Bank emer...",
            url = "https://economictimes.indiatimes.com/markets/stocks/news/mcap-of-seven-of-top-10-most-valued-firms-climb-rs-67859-77-cr-icici-bank-hdfc-bank-shine/articleshow/99529850.cms",
            imageUrl = "https://img.etimg.com/thumb/msid-99529868,width-1070,height-580,imgsize-32610,overlay-etmarkets/photo.jpg",
            source = "economictimes.indiatimes.com",
            dateStr = "2023-04-16T05:17:59.000000Z"
        )
    )

    ThreeDotsLayout("News") {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .verticalScroll(scrollState),
        ) {
            news.forEach {
                Spacer(modifier = Modifier.height(12.dp))
                NewsItemRepresentation(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    PreviewWrapper {
        NewsScreen()
    }
}