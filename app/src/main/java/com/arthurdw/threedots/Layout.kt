package com.arthurdw.threedots

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arthurdw.threedots.ui.theme.ThreeDotsTheme

@Composable
fun QuickNav() {
    @Composable
    fun NavItem(
        @DrawableRes icon: Int,
        label: String,
        selected: Boolean,
        onClick: () -> Unit,
        changeColor: Boolean = true
    ) {
        val painter = painterResource(icon)
        Image(painter,
            label,
            modifier = Modifier
                .clickable { onClick() }
                .graphicsLayer(alpha = if (selected) 1f else 0.75f),
            colorFilter = if (changeColor) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null)
    }


    Box(modifier = Modifier.fillMaxWidth()) {
        Divider(
            thickness = 4.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(R.drawable.ic_stocks, "Stocks", false, {})
            NavItem(R.drawable.ic_dots, "Home", true, {}, changeColor = false)
            NavItem(R.drawable.ic_news, "News", false, {})
        }
    }
}

@Composable
fun SideNav() {
    Image(
        painterResource(R.drawable.ic_nav),
        "Show more navigation",
        modifier = Modifier.padding(top = 30.dp, start = 30.dp).width(50.dp).height(50.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeDotsLayout(content: @Composable () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = { SideNav() }, bottomBar = { QuickNav() }, content = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                content()
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun ThreeDotsLayoutPreview() {
    ThreeDotsTheme {
        ThreeDotsLayout {
            Text(text = "Hello World!")
        }
    }
}