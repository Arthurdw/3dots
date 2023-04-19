package com.arthurdw.threedots.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.InputField
import com.arthurdw.threedots.components.Stock
import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.utils.PreviewWrapper


@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
) {
    var query by remember { mutableStateOf("") }
    val searchIcon = painterResource(R.drawable.ic_search)
    val clearIcon = painterResource(R.drawable.ic_exit)


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InputField(
            onComplete = onSearch,
            placeholder = placeholder,
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Image(
                        painter = clearIcon,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                query = ""
                                onClear()
                            },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                } else {
                    Image(
                        painter = searchIcon,
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onSearch(query) },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                }
            },
            imeAction = ImeAction.Search,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}


@Composable
fun StocksScreen() {
    val scrollState = rememberScrollState()

    val stocks = listOf(
        BasicStock("Alibaba", "BABA", 219.68f, 214.26f),
        BasicStock("Alphabet", "GOOGL", 93.65f, 101.7f),
        BasicStock("Amazon", "AMZN", 331.48f, 309.04f),
        BasicStock("Apple", "AAPL", 168.56f, 161.4f),
        BasicStock("Coca-Cola", "KO", 54.68f, 53.02f),
        BasicStock("Facebook", "FB", 311.54f, 304.67f),
        BasicStock("Johnson & Johnson", "JNJ", 162.07f, 160.22f),
        BasicStock("Microsoft", "MSFT", 264.11f, 266.71f),
        BasicStock("NVIDIA", "NVDA", 618.71f, 601.00f),
        BasicStock("Netflix", "NFLX", 503.28f, 498.33f),
        BasicStock("Procter & Gamble", "PG", 135.79f, 132.18f),
        BasicStock("Tesla", "TSLA", 710.44f, 699.68f)
    )

    ThreeDotsLayout("Stocks") {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            SearchBar(onSearch = { /* TODO */
                Log.d("3dots", "StocksScreen: $it")
            }, onClear = { /*TODO*/
                Log.d("3dots", "StocksScreen: CLEAR")
            })
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState),
            ) {
                stocks.forEach {
                    Stock(it, modifier = Modifier.fillMaxWidth(0.9f))
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StocksScreenPreview() {
    PreviewWrapper {
        StocksScreen()
    }
}