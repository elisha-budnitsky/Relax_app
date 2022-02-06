package com.breaktime.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.breaktime.lab3.navigation.EnteringNavGraph
import com.breaktime.lab3.ui.theme.Lab3Theme

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab3Theme {
                navController = rememberNavController()
                EnteringNavGraph(navController = navController)
            }
        }
    }
}