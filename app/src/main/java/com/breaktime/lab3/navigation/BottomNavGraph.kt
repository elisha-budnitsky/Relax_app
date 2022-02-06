package com.breaktime.lab3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.breaktime.lab3.view.listen.ListenScreen
import com.breaktime.lab3.view.main.BottomBarScreen
import com.breaktime.lab3.view.menu.MenuScreen
import com.breaktime.lab3.view.profile.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(Screen.Home.route) {
            MenuScreen(navController = navController)
        }
        composable(Screen.Listen.route) {
            ListenScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}