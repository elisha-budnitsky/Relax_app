package com.breaktime.lab3.view.guide

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GuideScreen(navController: NavHostController) {
    TabLayoutDemo()
}

class MainActivity : ComponentActivity() {

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            TabLayoutDemo()
        }
    }
}

private val tabs = listOf(
    TabItem.Home,
    TabItem.Settings,
    TabItem.Contacts
)

sealed class TabItem(
    val index: Int,
    val icon: ImageVector,
    val title: String,
    val screenToLoad: @Composable () -> Unit
) {
    object Home : TabItem(0, Icons.Default.Home, "Home", {
        HomeScreenForTab()
    })

    object Contacts : TabItem(2, Icons.Default.Info, "Contacts", {
        ContactScreenForTab()
    })

    object Settings : TabItem(1, Icons.Default.Settings, "Settings", {
        SettingsScreenForTab()
    })
}

@Composable
fun HomeScreenForTab() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.home),
            contentDescription = "home",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(700.dp)
        )
        Text(
            text = "That is home scree, where you can select you mood and read recommendation",
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun ContactScreenForTab() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.music),
            contentDescription = "music",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(700.dp)
        )
        Text(
            text = "Music screen with recommendation",
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun SettingsScreenForTab() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "profile",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(700.dp)
        )
        Text(
            text = "Profile screen, where you can upload images",
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@ExperimentalPagerApi
@Composable
fun TabLayoutDemo() {
    val pagerState = rememberPagerState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            TabPage(tabItems = tabs, pagerState = pagerState)
        }
    )
}

@ExperimentalPagerApi
@Composable
fun TabPage(pagerState: PagerState, tabItems: List<TabItem>) {
    HorizontalPager(
        count = tabs.size,
        state = pagerState
    ) { index ->
        tabItems[index].screenToLoad()
    }
}

@ExperimentalPagerApi
@Composable
fun IconWithTextTabLayout(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onPageSelected: ((tabItem: TabItem) -> Unit)
) {
    TabRow(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(selected = index == selectedIndex, onClick = {
                onPageSelected(tabItem)
            }, text = {
                Text(text = tabItem.title)
            }, icon = {
                Icon(tabItem.icon, "")
            })
        }
    }
}

@Composable
fun TextTabLayout(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onPageSelected: ((tabItem: TabItem) -> Unit)
) {
    TabRow(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(selected = index == selectedIndex, onClick = {
                onPageSelected(tabItem)
            }, text = {
                Text(text = tabItem.title)
            })
        }
    }
}

@Composable
fun ScrollableTabLayout(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onPageSelected: ((tabItem: TabItem) -> Unit)
) {
    ScrollableTabRow(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(selected = index == selectedIndex, onClick = {
                onPageSelected(tabItem)
            }, text = {
                Text(text = tabItem.title)
            }, icon = {
                Icon(tabItem.icon, "")
            })
        }
    }
}

@Composable
fun IconTabLayout(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onPageSelected: ((tabItem: TabItem) -> Unit)
) {
    TabRow(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(selected = index == selectedIndex, onClick = {
                onPageSelected(tabItem)
            }, icon = {
                Icon(tabItem.icon, "")
            })
        }
    }
}

@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TabLayoutDemo()
}