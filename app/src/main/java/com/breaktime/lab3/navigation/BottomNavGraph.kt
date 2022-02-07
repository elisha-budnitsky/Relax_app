package com.breaktime.lab3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.breaktime.lab3.view.home.HomeScreen
import com.breaktime.lab3.view.listen.ListenScreen
import com.breaktime.lab3.view.main.BottomBarScreen
import com.breaktime.lab3.view.profile.ProfileScreen

@Composable
fun BottomNavGraph(bottomNavController: NavHostController, navController: NavHostController) {
    NavHost(navController = bottomNavController, startDestination = BottomBarScreen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Listen.route) {
            ListenScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}