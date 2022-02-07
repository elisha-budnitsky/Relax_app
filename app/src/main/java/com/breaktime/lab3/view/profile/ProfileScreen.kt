package com.breaktime.lab3.view.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R
import com.breaktime.lab3.navigation.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3839))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu_24),
                    contentDescription = "logo",
                    tint = Color.White
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier.size(130.dp),
                tint = Color.White
            )
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "exit", fontSize = 18.sp, color = Color.White)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Name", fontSize = 24.sp, color = Color.White)
                Text("Age: 20", fontSize = 18.sp, color = Color.White)
                Text("Weight: 70kg", fontSize = 18.sp, color = Color.White)
                Text("Pressure: 120", fontSize = 18.sp, color = Color.White)
                Text("Phone: 120-80-90", fontSize = 18.sp, color = Color.White)
            }
        }

        val data = listOf(
            "Item 1",
            "Item 2",
            "Item 3",
        )

        LazyVerticalGrid(
            modifier = Modifier.padding(bottom = 50.dp),
            cells = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            data.forEach { item ->
                item {
                    TextButton(
                        onClick = { navController.navigate(Screen.Photo.route) },
                        contentPadding = PaddingValues()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(7.dp),
                            shape = RoundedCornerShape(20.dp),
                            backgroundColor = Color.LightGray
                        ) {
                            Image(
                                painter = painterResource(R.drawable.background),
                                contentDescription = "photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = item,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Left,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp, top = 90.dp)
                            )
                        }
                    }
                }
            }
            item {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(7.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7AAD77)),
                    onClick = { }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+", fontSize = 40.sp, color = Color.White)
                    }
                }
            }
        }
    }
}