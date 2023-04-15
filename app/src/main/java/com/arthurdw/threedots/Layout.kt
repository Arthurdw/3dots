package com.arthurdw.threedots

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
fun SideNav(onOpen: () -> Unit) {
    Image(
        painterResource(R.drawable.ic_nav),
        "Show more navigation",
        modifier = Modifier
            .padding(top = 30.dp, start = 30.dp)
            .width(50.dp)
            .height(50.dp)
            .clickable { onOpen() },
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun VerticalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f))
    )
}

@Composable
fun Sidebar(onClose: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    @Composable
    fun NavIcon(
        @DrawableRes icon: Int, label: String, onClick: () -> Unit, modifier: Modifier = Modifier
    ) {
        Image(painterResource(icon),
            label,
            modifier = modifier
                .clickable { onClick() }
                .width(50.dp)
                .height(50.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary))
    }

    Row(
        modifier = Modifier
            .height(screenHeight - 99.dp)
            .background(MaterialTheme.colorScheme.background)
            .zIndex(1000f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .fillMaxWidth(0.75f),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            NavIcon(R.drawable.ic_exit,
                "Close",
                {},
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .clickable { onClose() })

            Column {
                val navItems = listOf("Home", "Stocks", "News", "Share", "Scan")

                navItems.forEach {
                    Text(
                        it,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavIcon(
                    R.drawable.ic_settings,
                    "Settings",
                    {},
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                NavIcon(
                    R.drawable.ic_logout, "Logout", {}, modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
        VerticalDivider(
            modifier = Modifier
                .width(4.dp)
                .zIndex(999999f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeDotsLayout(content: @Composable () -> Unit) {
    val isSidebarOpen = remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            SideNav { isSidebarOpen.value = true }
        }, bottomBar = { QuickNav() }, content = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                content()
            }

            if (isSidebarOpen.value) Sidebar { isSidebarOpen.value = false }
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