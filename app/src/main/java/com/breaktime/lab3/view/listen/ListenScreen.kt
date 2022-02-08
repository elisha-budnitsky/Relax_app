package com.breaktime.lab3.view.listen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R

@Composable
fun ListenScreen(navController: NavHostController) {
    Listen()
}

@Composable
private fun Listen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3839))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Fun music",
                fontSize = 34.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                color = Color.White
            )
            Text(
                text = "recommendation",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                color = Color.White
            )
            Image(
                painter = painterResource(R.drawable.music_logo),
                contentDescription = "background",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(vertical = 60.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "00:00", color = Color.White)
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    progress = 0.5f
                )
                Text(text = "00:00", color = Color.White)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 60.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_shuffle),
                    contentDescription = "logo",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.ic_skip_previous),
                    contentDescription = "logo",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = "logo",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.ic_skip_next),
                    contentDescription = "logo",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.ic_repeat),
                    contentDescription = "logo",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun preview1() {
    Listen()
}