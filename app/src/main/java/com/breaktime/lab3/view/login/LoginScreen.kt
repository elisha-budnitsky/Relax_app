package com.breaktime.lab3.view.login

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R
import com.breaktime.lab3.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel = get<LoginViewModel>()
    val context = LocalContext.current
    val isLoadingState = remember { mutableStateOf(false) }
    initObservable(rememberCoroutineScope(), context, viewModel, isLoadingState, navController)
    var email by remember { mutableStateOf("") }
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
                text = "Sign in",
                color = Color.White,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(horizontal = 25.dp),
                value = email,
                onValueChange = { email = it },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    viewModel.setEvent(LoginContract.Event.OnResetPasswordButtonClick(email))
                }) {
                    Text(
                        modifier = Modifier.padding(horizontal = 25.dp),
                        text = "Forgot password?",
                        color = Color(0xFF0060C5),
                        fontSize = 14.sp
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .padding(top = 40.dp)
                    .alpha(0.8f),
                onClick = {
                    viewModel.setEvent(
                        LoginContract.Event.OnAuthButtonClick(email, password)
                    )
                }
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "Sign in",
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
                    text = "Don't have an account?",
                    color = Color.White,
                    fontSize = 16.sp
                )
                TextButton(
                    onClick = {
                        viewModel.setEvent(LoginContract.Event.OnRegisterButtonClick)
                    }
                )
                {
                    Text(
                        text = "Sign up",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
            }
        }
        if (isLoadingState.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(60.dp))
            }
        }
    }
}

private fun initObservable(
    composableScope: CoroutineScope,
    context: Context,
    viewModel: LoginViewModel,
    isLoadingState: MutableState<Boolean>,
    navController: NavHostController
) {
    composableScope.launch {
        viewModel.uiState.collect {
            composableScope.ensureActive()
            when (it.loginState) {
                is LoginContract.LoginState.Menu -> {
                    navController.popBackStack()
                    navController.navigate(Screen.Main.route)
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is LoginContract.LoginState.FirstEnter -> {
                    navController.popBackStack()
                    navController.navigate(Screen.FirstEnter.route)
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is LoginContract.LoginState.Register -> {
                    navController.popBackStack()
                    navController.navigate(Screen.Registration.route)
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is LoginContract.LoginState.Idle -> {
                    isLoadingState.value = false
                }
                is LoginContract.LoginState.Loading -> {
                    isLoadingState.value = true
                }
            }
        }
    }
    composableScope.launch {
        viewModel.effect.collect {
            composableScope.ensureActive()
            when (it) {
                is LoginContract.Effect.ShowIncorrectDataToast -> {
                    Toast.makeText(
                        context, it.message, Toast.LENGTH_SHORT
                    ).show()
                }
                is LoginContract.Effect.ShowWrongParamsToast -> {
                    Toast.makeText(
                        context, "Incorrect fields data", Toast.LENGTH_SHORT
                    ).show()
                }
                is LoginContract.Effect.ShowCheckEmailToast -> {
                    Toast.makeText(
                        context, "Check your email", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}