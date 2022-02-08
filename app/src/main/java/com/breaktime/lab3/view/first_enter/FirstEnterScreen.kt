package com.breaktime.lab3.view.first_enter

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// TODO: come from sing up or if user don't have saved info
@Composable
fun FirstEnterScreen(navController: NavHostController) {
    val viewModel = get<FirstEnterViewModel>()
    val isLoadingState = remember { mutableStateOf(false) }
    initObservable(
        rememberCoroutineScope(),
        viewModel,
        isLoadingState,
        navController
    )
    val phoneNumber = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val pressure = remember { mutableStateOf("") }
    val birthday = remember { mutableStateOf("") }
    val phoneNumberData =
        ScreenData(
            "Enter your phone number",
            R.drawable.phone,
            "phone number",
            "next",
            phoneNumber,
            ::checkNumber
        )
    val weightData =
        ScreenData(
            "Enter your weight",
            R.drawable.weight,
            "weight",
            "next",
            weight,
            ::check3Number
        )
    val pressureData =
        ScreenData(
            "Enter your pressure",
            R.drawable.weight,
            "pressure",
            "next",
            pressure,
            ::check3Number
        )
    val birthdayData =
        ScreenData(
            "Enter your birthday date",
            R.drawable.calendar,
            "08.03.1998",
            "finish",
            birthday,
            ::checkBirthday
        )

    when {
        phoneNumber.value.isEmpty() -> {
            GetDataScreen(screenData = phoneNumberData)
        }
        weight.value.isEmpty() -> {
            GetDataScreen(screenData = weightData)
        }
        pressure.value.isEmpty() -> {
            GetDataScreen(screenData = pressureData)
        }
        birthday.value.isEmpty() -> {
            GetDataScreen(screenData = birthdayData)
        }
        else -> {
            viewModel.setEvent(
                FirstEnterContract.Event.OnFinishButtonClick(
                    phoneNumber.value,
                    weight.value,
                    pressure.value,
                    birthday.value
                )
            )
            phoneNumber.value = ""
            weight.value = ""
            pressure.value = ""
            birthday.value = ""

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

@Composable
private fun GetDataScreen(screenData: ScreenData) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF283234)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = screenData.title,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Image(
                painter = painterResource(screenData.image),
                contentDescription = "avatar",
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .height(150.dp)
                    .fillMaxWidth()
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .padding(horizontal = 25.dp),
                value = text,
                onValueChange = { text = it },
                label = { Text(screenData.fieldLabel, color = MaterialTheme.colors.primary) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .padding(top = 40.dp)
                    .alpha(0.8f),
                onClick = {
                    if (screenData.check(text))
                        screenData.returnValue.value = text
                    else
                        Toast.makeText(context, "Check data", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = screenData.buttonText,
                    color = Color.White,
                    fontSize = 22.sp
                )
            }
        }
    }
}

private fun initObservable(
    composableScope: CoroutineScope,
    viewModel: FirstEnterViewModel,
    isLoadingState: MutableState<Boolean>,
    navController: NavHostController
) {
    composableScope.launch {
        viewModel.uiState.collect {
            composableScope.ensureActive()
            when (it.firstEnterState) {
                is FirstEnterContract.FirstEnterState.Success -> {
                    navController.popBackStack()
                    navController.navigate(Screen.Main.route)
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is FirstEnterContract.FirstEnterState.Idle -> {
                    isLoadingState.value = false
                }
                is FirstEnterContract.FirstEnterState.Loading -> {
                    isLoadingState.value = true
                }
            }
        }
    }
}

private data class ScreenData(
    val title: String,
    val image: Int,
    val fieldLabel: String,
    val buttonText: String,
    val returnValue: MutableState<String>,
    val check: (String) -> Boolean
)