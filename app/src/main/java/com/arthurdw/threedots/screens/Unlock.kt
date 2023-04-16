package com.arthurdw.threedots.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.arthurdw.threedots.R
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme

val insertedState by lazy { mutableStateListOf<String>() }

@Composable
fun UnlockKey(key: String, modifier: Modifier = Modifier) {
    Text(
        key,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = modifier.clickable {
            when (key) {
                "C" -> {
                    insertedState.clear()
                }

                "DEL" -> {
                    insertedState.removeLastOrNull()
                }

                else -> {
                    if (insertedState.size < 5) {
                        insertedState.add(key)
                    }
                }
            }
        }
    )
}

@Composable
fun UnlockKeyboard(modifier: Modifier = Modifier) {
    val chars = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "C", "DEL")

    LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
        items(chars.size) { index ->
            UnlockKey(chars[index], modifier = Modifier.padding(top = 20.dp, bottom = 20.dp))
        }
    }, modifier = modifier)
}

@Composable
fun Dot(modifier: Modifier = Modifier, isEmpty: Boolean = false) {
    val fore = MaterialTheme.colorScheme.primary
    val back = MaterialTheme.colorScheme.background
    val size = 32.dp

    Surface(modifier = modifier.padding(10.dp), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .size(size)
                .background(color = fore, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isEmpty) {
                Box(
                    modifier = Modifier
                        .size(size * 0.75f)
                        .background(color = back, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
fun UnlockDots(amount: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        repeat(amount) { Dot(isEmpty = false) }
        repeat(5 - amount) { Dot(isEmpty = true) }
    }
}


@Composable
fun Unlock(text: String?, onSuccess: () -> Unit = {}) {
    val icon = painterResource(id = R.drawable.ic_dots)

    if (insertedState.size == 5) {
        val joined = insertedState.joinToString("")

        // TODO: Check for hash
        if (joined == "11111") {
            onSuccess()
            insertedState.clear()
        } else {
            insertedState.clear()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = icon,
                contentDescription = "Icon",
                modifier = Modifier
                    .size(200.dp)
                    .background(color = MaterialTheme.colorScheme.background),

                )

            if (text != null) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

            Column(
                modifier = Modifier.padding(top = 100.dp, bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UnlockDots(insertedState.size)

                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(300.dp),
                ) {
                    UnlockKeyboard()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockPreview() {
    ThreeDotsTheme {
        Unlock(text = "Welcome back Arthur!")
    }
}