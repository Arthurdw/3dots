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
import androidx.compose.foundation.layout.size
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
import com.arthurdw.threedots.ui.Screens
import com.arthurdw.threedots.utils.PreviewWrapper
import com.arthurdw.threedots.utils.State

@Composable
fun QuickNav() {
    val navController = State.NavController.current

    @Composable
    fun NavItem(
        @DrawableRes icon: Int,
        label: String,
        selected: Boolean,
        onClick: () -> Unit,
        changeColor: Boolean = true,
    ) {
        val painter = painterResource(icon)
        Image(painter,
            label,
            modifier = Modifier
                .size(60.dp)
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
            NavItem(
                R.drawable.ic_stocks,
                "Stocks",
                false,
                { navController.navigate(Screens.Stocks.route) }
            )
            NavItem(
                R.drawable.ic_dots,
                "Home",
                true,
                { navController.navigate(Screens.Overview.route) },
                changeColor = false,
            )
            NavItem(
                R.drawable.ic_news,
                "News",
                false,
                { navController.navigate(Screens.News.route) }
            )
        }
    }
}

@Composable
fun SideNav(title: String? = null, onOpen: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Row(
        modifier = Modifier.padding(top = 30.dp, start = 30.dp)
    ) {
        Image(
            painterResource(R.drawable.ic_nav),
            "Show more navigation",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clickable { onOpen() },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )

        if (title != null) {
            Text(
                title,
                modifier = Modifier
                    .width(screenWidth - 160.dp)
                    .padding(top = 20.dp),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }
    }
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
    val navController = State.NavController.current

    data class NavItem(val text: String, val screen: Screens)

    @Composable
    fun NavIcon(
        @DrawableRes icon: Int,
        label: String,
        screen: Screens? = null,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {}
    ) {
        Image(painterResource(icon),
            label,
            modifier = modifier
                .clickable {
                    onClick()
                    if (screen != null) {
                        navController.navigate(screen.route)
                    }
                    onClose()
                }
                .width(50.dp)
                .height(50.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary))
    }

    Row(
        modifier = Modifier
            .height(screenHeight - 116.dp)
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
            NavIcon(
                R.drawable.ic_exit,
                "Close",
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            )

            Column {
                val navItems = listOf(
                    NavItem("Home", Screens.Overview),
                    NavItem("Stocks", Screens.Stocks),
                    NavItem("News", Screens.News),
                    NavItem("Share", Screens.Share),
                    NavItem("Scan", Screens.Scan)
                )

                navItems.forEach {
                    Text(
                        it.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(it.screen.route)
                                onClose()
                            },
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
                    Screens.Settings,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                NavIcon(
                    R.drawable.ic_logout,
                    "Logout",
                    Screens.SignInWith,
                    modifier = Modifier.padding(bottom = 16.dp),
                    onClick = {
                        // TODO: Logout
                    }
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
fun ThreeDotsLayout(title: String? = null, content: @Composable () -> Unit) {
    val isSidebarOpen = remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            SideNav(title) { isSidebarOpen.value = true }
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
    PreviewWrapper {
        ThreeDotsLayout(title = "Hello world!") {
            Text(text = "Hello World!")
        }
    }
}