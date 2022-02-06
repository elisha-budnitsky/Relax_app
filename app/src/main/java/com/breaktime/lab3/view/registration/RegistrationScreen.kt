package com.breaktime.lab3.view.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R
import com.breaktime.lab3.navigation.Screen

@Composable
fun RegistrationScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF283234))
    ) {
        Image(
            painter = painterResource(R.drawable.leaf),
            contentDescription = "leaf",
            alignment = Alignment.BottomEnd,
            contentScale = ContentScale.None,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "logo",
                alignment = Alignment.BottomEnd,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(100.dp)
            )
            Text(
                modifier = Modifier.padding(horizontal = 25.dp),
                text = "Sign up",
                color = Color.White,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                value = name,
                onValueChange = { name = it },
                label = { Text("Name", color = MaterialTheme.colors.primary) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White
                )
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(horizontal = 25.dp),
                value = login,
                onValueChange = { login = it },
                label = { Text("Email", color = MaterialTheme.colors.primary) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(horizontal = 25.dp),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = MaterialTheme.colors.primary) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .padding(top = 40.dp)
                    .alpha(0.8f),
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Screen.Main.route)
                }
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "Sign up",
                    color = Color.White,
                    fontSize = 22.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    color = Color.White,
                    fontSize = 16.sp
                )
                TextButton(
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(Screen.Login.route)
                    }
                )
                {
                    Text(
                        text = "Sign in",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}