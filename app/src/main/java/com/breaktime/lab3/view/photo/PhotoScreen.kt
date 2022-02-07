package com.breaktime.lab3.view.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R

@Composable
fun PhotoScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3839))
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "photo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "delete",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "close",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}