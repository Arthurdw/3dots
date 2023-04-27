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
import com.arthurdw.threedots.ThreeDotsLayout
import com.arthurdw.threedots.components.InputField
import com.arthurdw.threedots.components.ManagedInputField
import com.arthurdw.threedots.objects.BasicStock
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.toCurrencyString

@Composable
fun LabeledText(label: String, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Text(text)
    }
}

@Composable
fun PickScreen(stockId: String, sell: Boolean = false) {
    val title = if (sell) "Sell" else "Buy"

    val stock = BasicStock(
        symbol = "APL",
        name = "Apple",
        price = 156.0f,
        lastPrice = 156.0f,
    )

    var price by remember { mutableStateOf(stock.price) }
    var amount by remember { mutableStateOf(1f) }
    var priceQuery by remember { mutableStateOf(price.toString()) }
    var amountQuery by remember { mutableStateOf(amount.toString()) }
    var editingPrice by remember { mutableStateOf(false) }
    var editingAmount by remember { mutableStateOf(false) }

    ThreeDotsLayout(title) {
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
                stock.symbol, stock.name
            )
//            }
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
                onClick = { /*TODO*/ },
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
}


@Preview(showBackground = true)
@Composable
fun PickScreenPreview() {
    PreviewWrapper {
        PickScreen("APL")
    }
}
