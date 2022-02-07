package com.breaktime.lab3.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R

@Composable
fun HomeScreen(navController: NavHostController) {
    Main()
}

@Composable
fun Main() {
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
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = "avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(43.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )
        }
        Text(
            text = "С возвращение, Эмиль!",
            fontSize = 32.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Text(
            text = "Каким ты себя ощущаешь сегодня?",
            fontSize = 20.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        val moodData = listOf(
            MoodInfo("Спокойным", R.drawable.ic_calm, true),
            MoodInfo("Расслабленным", R.drawable.ic_relax, false),
            MoodInfo("Сосредоточенным", R.drawable.ic_focus, false),
            MoodInfo("Взволнованным", R.drawable.ic_excited, false),
            MoodInfo("Веселым", R.drawable.ic_fun, false),
            MoodInfo("Грустным", R.drawable.ic_sadness, false)
        )
        LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
            moodData.forEach {
                item {
                    MoodItem(moodInfo = it)
                }
            }
        }

        val suggestionData = listOf(
            SuggestionInfo(
                "Current recommendation",
                "Description bla bla bla bla bla",
                "Content bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla",
                R.drawable.current_recommendation
            ),
            SuggestionInfo(
                "Daily recommendation",
                "Description bla bla bla bla bla",
                "Content bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla",
                R.drawable.daily_recommendation
            ),

            SuggestionInfo(
                "Horoscope",
                "Description bla bla bla bla bla",
                "Content bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla",
                R.drawable.horoscope
            )
        )
        LazyColumn(modifier = Modifier.padding(bottom = 60.dp)) {
            suggestionData.forEach {
                item {
                    ExpandableItem(it)
                }
            }
        }
    }
}

@Preview
@Composable
fun preview() {
    Main()
}