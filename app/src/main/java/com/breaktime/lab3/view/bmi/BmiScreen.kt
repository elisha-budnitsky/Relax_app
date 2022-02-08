package com.breaktime.lab3.view.bmi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlin.math.roundToInt

@Composable
fun BmiScreen(navController: NavHostController) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3839)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            TextField(
                modifier = Modifier
                    .width(200.dp),
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight", color = MaterialTheme.colors.primary) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                modifier = Modifier
                    .width(200.dp),
                value = height,
                onValueChange = { height = it },
                label = { Text("Height", color = MaterialTheme.colors.primary) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                onClick = {}
            ) {
                Text(text = "Calculate")
            }

            if (weight.isNotEmpty() && height.isNotEmpty()) {
                val bmi =
                    (1000000 * weight.toDouble() / (height.toDouble() * height.toDouble())).roundToInt() / 100f

                val result = when {
                    bmi < 18.5 -> {
                        "Below normal weight"
                    }
                    bmi >= 18.5 && bmi < 25 -> {
                        "Normal weight"
                    }
                    bmi >= 25 && bmi < 30 -> {
                        "Overweight"
                    }
                    bmi >= 30 && bmi < 35 -> {
                        "Obesity I degree"
                    }
                    bmi >= 35 && bmi < 40 -> {
                        "Obesity II degree"
                    }
                    bmi >= 40 -> {
                        "Obesity III degree"
                    }
                    else -> {
                        "error"
                    }
                }

                Text(
                    text = (bmi).toString(),
                    modifier = Modifier
                        .width(200.dp),
                    textAlign = TextAlign.Center, color = Color.White, fontSize = 20.sp
                )

                Text(
                    text = result,
                    modifier = Modifier
                        .width(200.dp).padding(top = 10.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}
