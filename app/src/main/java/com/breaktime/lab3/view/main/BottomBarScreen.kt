package com.breaktime.lab3.view.main

import com.breaktime.lab3.R

sealed class BottomBarScreen(val route: String, val icon: Int) {
    object Home : BottomBarScreen(
        route = "home_screen",
        icon = R.drawable.ic_home_24
    )

    object Music : BottomBarScreen(
        route = "listen_screen",
        icon = R.drawable.ic_music_24
    )

    object Profile : BottomBarScreen(
        route = "profile_screen",
        icon = R.drawable.ic_profile_24
    )
}