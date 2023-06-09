package com.arthurdw.threedots.ui.screens.stocks

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.InputField
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.components.Stock
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.empty


@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    placeholderStr: String? = null,
) {
    var query by remember { mutableStateOf(String.empty()) }
    val searchIcon = painterResource(R.drawable.ic_search)
    val clearIcon = painterResource(R.drawable.ic_exit)
    val placeholder = placeholderStr ?: stringResource(R.string.search)


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InputField(
            value = query,
            onComplete = onSearch,
            placeholder = placeholder,
            onValueChange = { query = it },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Image(
                        painter = clearIcon,
                        contentDescription = stringResource(R.string.clear),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                query = String.empty()
                                onClear()
                            },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                } else {
                    Image(
                        painter = searchIcon,
                        contentDescription = stringResource(R.string.search),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onSearch(query) },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                }
            },
            imeAction = ImeAction.Search,
            modifier = Modifier.fillMaxWidth(0.9f),
        )
    }
}


@Composable
fun StocksScreen(
    modifier: Modifier = Modifier,
    stocksViewModel: StocksViewModel = viewModel(factory = StocksViewModel.Factory)
) {
    val scrollState = rememberScrollState()
    val state = stocksViewModel.state
    var query by remember { mutableStateOf(String.empty()) }


    ThreeDotsLayout(stringResource(R.string.stocks)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            SearchBar(
                onSearch = {
                    stocksViewModel.searchStocks(it)
                    query = it
                },
                onClear = {
                    stocksViewModel.searchStocks(String.empty())
                    query = String.empty()
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            when (state) {
                is StocksState.Loading -> Loading()
                is StocksState.Error -> {
                    Text(
                        stringResource(R.string.failed_to_load_stocks),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    TextButton(onClick = { stocksViewModel.searchStocks(query) }) {
                        Text(text = stringResource(R.string.retry))
                    }
                }

                is StocksState.Success -> {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState),
                    ) {
                        state.stocks.forEach {
                            Stock(it, modifier = Modifier.fillMaxWidth(0.9f))
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
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