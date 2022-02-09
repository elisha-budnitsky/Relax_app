package com.breaktime.lab3.view.edit_profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.breaktime.lab3.R
import com.breaktime.lab3.data.User
import com.breaktime.lab3.data.icon
import com.breaktime.lab3.firebase.Firebase
import org.koin.androidx.compose.get
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditScreen(navController: NavHostController) {
    var name by remember { mutableStateOf(User.name) }
    var phoneNumber by remember { mutableStateOf(User.phone) }
    var weight by remember { mutableStateOf(User.weight) }
    var pressure by remember { mutableStateOf(User.pressure) }
    var birthday by remember { mutableStateOf(User.birthday) }
    var loading by remember { mutableStateOf(false) }
    var updateIcon by remember { mutableStateOf(true) }
    val firebase = get<Firebase>()
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
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
            val pickPictureLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { imageUri ->
                if (imageUri != null) {
                    firebase.saveUserIcon(imageUri, onSuccess = {
                        updateIcon = false
                        User.icon = it
                        updateIcon = true
                    })
                }
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                if (updateIcon)
                    Image(
                        painter = if (User.icon == null) painterResource(R.drawable.no_photo)
                        else rememberImagePainter(User.icon),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                            .clickable {
                                pickPictureLauncher.launch("image/*")
                            }
                    )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .padding(horizontal = 25.dp),
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter name", color = MaterialTheme.colors.primary) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White
                )
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
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
                    .padding(top = 20.dp)
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
                    .padding(top = 20.dp)
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
                    .padding(top = 20.dp)
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
                    loading = true
                    if (checkNumber(phoneNumber) && check3Number(weight)
                        && check3Number(pressure) && checkBirthday(birthday)
                    ) {
                        val oldName = User.name
                        val oldPhone = User.phone
                        val oldWeight = User.weight
                        val oldPressure = User.pressure
                        val oldBirthday = User.birthday
                        User.name = name
                        User.phone = name
                        User.weight = name
                        User.pressure = name
                        User.birthday = name
                        firebase.saveUserData(
                            user = User,
                            onSuccess = {
                                navController.popBackStack()
                            },
                            onError = {
                                User.name = oldName
                                User.phone = oldPhone
                                User.weight = oldWeight
                                User.pressure = oldPressure
                                User.birthday = oldBirthday
                            }
                        )
                    } else {
                        Toast.makeText(context, "Check your data", Toast.LENGTH_SHORT).show()
                    }
                    loading = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 130.dp, vertical = 40.dp)
            ) {
                Text("Update info")
            }
        }
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(60.dp))
            }
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