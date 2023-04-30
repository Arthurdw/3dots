package com.arthurdw.threedots.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.components.ManagedInputField
import com.arthurdw.threedots.data.objects.StockWorth
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.ui.screens.pick.PickState
import com.arthurdw.threedots.ui.screens.pick.PickViewModel
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.utils.toCurrencyString

@Composable
fun LabeledText(label: String, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Text(text)
    }
}

@Composable
fun PickScreen(
    stockId: String,
    stock: StockWorth,
    sell: Boolean = false,
    onAction: (String, Float) -> Unit
) {

    var price by remember { mutableStateOf(stock.price) }
    var amount by remember { mutableStateOf(1f) }
    var priceQuery by remember { mutableStateOf(price.toString()) }
    var amountQuery by remember { mutableStateOf(amount.toString()) }
    var editingPrice by remember { mutableStateOf(false) }
    var editingAmount by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            "Stock",
            modifier = Modifier.padding(top = 32.dp),
            fontWeight = FontWeight.Bold,
        )
        LabeledText(
            stock.symbol, stock.stockName
        )
        Text("Price", fontWeight = FontWeight.Bold)
        if (editingPrice) {
            editingAmount = false
            ManagedInputField(
                onComplete = {
                    val changed = it.toFloat()

                    if (changed != price) {
                        price = changed
                        amount = price / stock.price
                        amountQuery = amount.toString()
                    }
                    editingPrice = false
                },
                value = priceQuery,
                keyboardType = KeyboardType.Decimal,
            )
        } else {
            Text(
                price.toCurrencyString(),
                modifier = Modifier
                    .clickable {
                        editingPrice = true
                    }
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        Text("Amount", fontWeight = FontWeight.Bold)
        if (editingAmount) {
            editingPrice = false
            ManagedInputField(
                onComplete = {
                    val changed = it.toFloat()

                    if (changed != amount) {
                        amount = changed
                        price = amount * stock.price
                        priceQuery = price.toString()
                    }
                    editingAmount = false
                },
                value = amountQuery,
                keyboardType = KeyboardType.Number,
            )
        } else {
            Text(
                amount.toString(),
                modifier = Modifier
                    .clickable {
                        editingAmount = true
                    }
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }

        TextButton(
            onClick = { onAction(stockId, amount) },
            colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
        ) {
            Text(
                "Confirm",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun PickScreen(
    stockId: String,
    sell: Boolean = false,
    pickViewModel: PickViewModel = viewModel(factory = PickViewModel.Factory),
) {
    val title = if (sell) "Sell" else "Buy"
    val uiState = pickViewModel.state
    val navController = State.NavController.current
    var requestedStockWorth by remember { mutableStateOf(false) }

    if (!requestedStockWorth) {
        pickViewModel.initStock(stockId)
        requestedStockWorth = true
    }

    ThreeDotsLayout(title) {
        when (uiState) {
            is PickState.Idle -> {
                PickScreen(
                    stockId = stockId,
                    sell = sell,
                    stock = uiState.stock,
                    onAction = if (sell) pickViewModel::sellStock else pickViewModel::buyStock
                )
            }

            is PickState.Loading -> Loading()
            is PickState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Failed to fetch prices", modifier = Modifier.padding(bottom = 12.dp)
                    )
                    TextButton(onClick = { pickViewModel.initStock(stockId) }) {
                        Text(text = "Retry")
                    }
                }
            }

            is PickState.Success -> navController.navigate(Screens.Overview.route)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PickScreenPreview() {
    PreviewWrapper {
        PickScreen("APL")
    }
}
