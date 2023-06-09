package com.arthurdw.threedots.ui.screens.unlock

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arthurdw.threedots.R
import com.arthurdw.threedots.components.Loading
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme
import com.arthurdw.threedots.utils.State
import com.arthurdw.threedots.utils.empty

val insertedState by lazy { mutableStateListOf<String>() }

@Composable
fun UnlockKey(
    key: String,
    clear: String = stringResource(R.string.keypad_clear),
    delete: String = stringResource(R.string.keypad_delete),
    modifier: Modifier = Modifier
) {
    Text(
        key,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = modifier.clickable {
            when (key) {
                clear ->  insertedState.clear()
                delete -> insertedState.removeLastOrNull()
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
    val clear = stringResource(R.string.keypad_clear)
    val delete = stringResource(R.string.keypad_delete)

    val chars = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", clear, delete)

    LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
        items(chars.size) { index ->
            UnlockKey(
                chars[index],
                clear = clear,
                delete = delete,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )
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
fun Unlock(
    text: String?,
    onUnlock: (code: String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    val icon = painterResource(id = R.drawable.ic_dots)

    if (insertedState.size == 5) {
        val joined = insertedState.joinToString(String.empty())
        insertedState.clear()
        onUnlock(joined)
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
                contentDescription = stringResource(R.string.icon),
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
                modifier = Modifier.padding(top = 70.dp, bottom = 50.dp),
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

@Composable
fun UnlockScreen(
    modifier: Modifier = Modifier,
    text: String? = null,
    unlockViewModel: UnlockViewModel = viewModel(factory = UnlockViewModel.Factory),
    disableCheck: Boolean = false,
    onSuccess: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val navController = State.LocalNavController.current

    fun unlock(code: String) {
        if (!disableCheck) unlockViewModel.unlock(context, code)
        else onSuccess(code)
    }
    when (val state = unlockViewModel.state) {
        is UnlockState.Loading -> Loading()
        is UnlockState.Error -> {
            Unlock(
                text = state.message,
                onUnlock = { unlock(it) },
                modifier = modifier,
            )
        }

        is UnlockState.Success -> navController.navigate(Screens.Overview.route)
        is UnlockState.Idle -> {
            Unlock(
                text = text,
                onUnlock = { unlock(it) },
                modifier = modifier,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockPreview() {
    ThreeDotsTheme {
        Unlock(text = "Welcome back ${State.CurrentUser.username}!")
    }
}