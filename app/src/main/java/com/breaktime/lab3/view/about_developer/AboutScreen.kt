package com.breaktime.lab3.view.about_developer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.ui.theme.pirataOne

@Composable
fun AboutScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3839))
            .padding(20.dp)
    ) {
        Text(
            text = "Budnitsky Elisey",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color.White
        )
        Text(
            text = "Break time",
            fontFamily = pirataOne,
            fontSize = 32.sp,
            color = Color.White
        )
        Text(
            text = "951006",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.White
        )
        Text(
            text = "Lab3",
            fontStyle = FontStyle.Italic,
            fontSize = 28.sp,
            color = Color.White
        )
    }
}