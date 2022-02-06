package com.breaktime.lab3.view.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun MenuScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFF7474)))
}