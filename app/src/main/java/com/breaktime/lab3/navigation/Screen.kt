package com.breaktime.lab3.navigation

sealed class Screen(val route: String) {
    object Splash : Screen(route = "splash_screen")
    object Login : Screen(route = "login_screen")
    object Registration : Screen(route = "registration_screen")
    object Home : Screen(route = "home_screen")
    object Main : Screen(route = "main_screen")
    object Menu : Screen(route = "menu_screen")
    object Photo : Screen(route = "photo_screen")
    object Listen : Screen(route = "listen_screen")
    object Profile : Screen(route = "profile_screen")
    object EditProfile : Screen(route = "edit_screen")
    object CalculateBmi : Screen(route = "calculate_mbi_screen")
    object AboutDeveloper : Screen(route = "about_screen")
    object Guide : Screen(route = "guide_screen")
}