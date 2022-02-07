package com.breaktime.lab3.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MoodItem(moodInfo: MoodInfo) {
    var isSelected by remember { mutableStateOf(moodInfo.isSelected) }
    TextButton(
        modifier = Modifier.padding(horizontal = 5.dp),
        onClick = {
            moodInfo.isSelected = !moodInfo.isSelected
            isSelected = !isSelected
        },
        contentPadding = PaddingValues()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .padding(7.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color.White,
                border = if (isSelected) BorderStroke(3.dp, Color(0xFFFF5F5F)) else null
            ) {
                Icon(
                    painter = painterResource(moodInfo.image),
                    contentDescription = "photo",
                    modifier = Modifier.padding(20.dp)
                )
            }
            Text(
                text = moodInfo.title,
                fontSize = 10.sp,
                textAlign = TextAlign.Left,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}