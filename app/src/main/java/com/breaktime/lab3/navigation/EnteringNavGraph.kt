package com.breaktime.lab3.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.breaktime.lab3.view.login.LoginScreen
import com.breaktime.lab3.view.main.MainScreen
import com.breaktime.lab3.view.menu.MenuScreen
import com.breaktime.lab3.view.photo.PhotoInfo
import com.breaktime.lab3.view.photo.PhotoScreen
import com.breaktime.lab3.view.registration.RegistrationScreen
import com.breaktime.lab3.view.splash.SplashScreen

@Composable
fun EnteringNavGraph(navController: NavHostController) {
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
        composable(Screen.Photo.route) {
            val info = navController.previousBackStackEntry?.savedStateHandle?.get<PhotoInfo>("info")
            info?.let {
                PhotoScreen(navController = navController, info = info)
            }
        }
        composable(Screen.Menu.route) {
            MenuScreen(navController = navController)
        }
    }
}
