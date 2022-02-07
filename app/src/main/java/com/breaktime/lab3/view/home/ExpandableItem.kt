package com.breaktime.lab3.view.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableItem(suggestionInfo: SuggestionInfo) {
    var expandableState by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearOutSlowInEasing
                )
            ),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = suggestionInfo.title, fontSize = 24.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
//                    Text(text = suggestionInfo.title, fontSize = 24.sp)
                    Text(
                        text = suggestionInfo.description,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
                    if (expandableState)
                        Text(
                            text = suggestionInfo.content,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(vertical = 3.dp),
                        onClick = { expandableState = !expandableState },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF2D3839),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = if (expandableState) "Less" else "More")
                    }
                }

                Image(
                    painter = painterResource(suggestionInfo.image),
                    contentDescription = "logo",
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                )
            }
        }
    }
}