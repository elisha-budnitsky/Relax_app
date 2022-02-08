package com.breaktime.lab3.view.edit_profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditScreen(navController: NavHostController) {
    Edit()
}

@Composable
fun Edit() {
    var phoneNumber by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var pressure by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3839))
    ) {
        Text(
            text = "Update profile",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.no_photo),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 25.dp),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Enter phone number", color = MaterialTheme.colors.primary) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.White,
                cursorColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 25.dp),
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter your weight", color = MaterialTheme.colors.primary) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.White,
                cursorColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 25.dp),
            value = pressure,
            onValueChange = { pressure = it },
            label = { Text("Enter your pressure", color = MaterialTheme.colors.primary) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.White,
                cursorColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 25.dp),
            value = birthday,
            onValueChange = { birthday = it },
            label = { Text("Enter your birthday date", color = MaterialTheme.colors.primary) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.White,
                cursorColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                if (checkNumber(phoneNumber) && check3Number(weight)
                    && check3Number(pressure) && checkBirthday(birthday)
                ) {
                    println("correct")
                    // TODO: save data
                    // TODO: navigate back
                } else {
                    Toast.makeText(context, "Check your data", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 130.dp, vertical = 40.dp)
        ) {
            Text("Update image")
        }
    }
}


fun checkBirthday(text: String): Boolean {
    return try {
        val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        df.isLenient = false
        df.parse(text)
        true
    } catch (e: ParseException) {
        false
    }
}

fun checkNumber(text: String): Boolean {
    return text.matches(Regex("[0-9]+"))
}

fun check3Number(text: String): Boolean {
    return text.matches(Regex("[0-9]{1,3}"))
}

@Preview
@Composable
fun padf() {
    Edit()
}