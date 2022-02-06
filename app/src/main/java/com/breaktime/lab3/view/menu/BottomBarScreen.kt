package com.breaktime.lab3.view.menu

import com.breaktime.lab3.R

sealed class BottomBarScreen(val route: String, val title: String, val icon: Int) {
    object Home : BottomBarScreen(
        route = "home_screen",
        title = "home",
        icon = R.drawable.ic_home_24
    )

    object Music : BottomBarScreen(
        route = "listen_screen",
        title = "music",
        icon = R.drawable.ic_music_24
    )

    object Profile : BottomBarScreen(
        route = "profile_screen",
        title = "profile",
        icon = R.drawable.ic_profile_24
    )
}