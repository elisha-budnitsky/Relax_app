package com.breaktime.lab3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.breaktime.lab3.view.listen.ListenScreen
import com.breaktime.lab3.view.login.LoginScreen
import com.breaktime.lab3.view.main.MainScreen
import com.breaktime.lab3.view.menu.MenuScreen
import com.breaktime.lab3.view.photo.PhotoScreen
import com.breaktime.lab3.view.profile.ProfileScreen
import com.breaktime.lab3.view.registration.RegistrationScreen
import com.breaktime.lab3.view.splash.SplashScreen

@Composable
fun SetUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController = navController)
        }
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }
        composable(Screen.Menu.route) {
            MenuScreen(navController = navController)
        }
        composable(Screen.Photo.route) {
            PhotoScreen(navController = navController)
        }
        composable(Screen.Listen.route) {
            ListenScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}
