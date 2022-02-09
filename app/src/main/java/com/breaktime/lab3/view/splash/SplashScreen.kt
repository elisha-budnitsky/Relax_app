package com.breaktime.lab3.view.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R
import com.breaktime.lab3.data.User
import com.breaktime.lab3.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.get

@Composable
fun SplashScreen(navController: NavHostController) {
    val auth = get<FirebaseAuth>()
    var startLogoAnimation by remember { mutableStateOf(false) }
    val alphaLogoAnim = animateFloatAsState(
        targetValue = if (startLogoAnimation) 0f else 1f,
        animationSpec = tween(durationMillis = 1000)
    )
    var startOnboardAnimation by remember { mutableStateOf(false) }
    val alphaOnboardAnim = animateFloatAsState(
        targetValue = if (startOnboardAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val scope = rememberCoroutineScope()
    val firebaseDatabase = get<FirebaseDatabase>()

    LaunchedEffect(key1 = true) {
        val client = async { auth.currentUser }
        delay(1000)
        val result = client.await()
        if (result != null) {
            loadUser(scope, auth, firebaseDatabase)
            delay(1000)
            navController.popBackStack()
            navController.navigate(Screen.Main.route)
        } else {
            delay(1000)
            startLogoAnimation = true
            delay(500)
            startOnboardAnimation = true
        }
    }
    Splash(alpha = alphaLogoAnim.value)
    Onboarding(
        alpha = alphaOnboardAnim.value,
        onClickLogin = {
            navController.popBackStack()
            navController.navigate(Screen.Login.route)
        },
        onClickRegister = {
            navController.popBackStack()
            navController.navigate(Screen.Registration.route)
        }
    )
}

private fun loadUser(
    scope: CoroutineScope,
    auth: FirebaseAuth,
    firebaseDatabase: FirebaseDatabase
) {
    val user = auth.currentUser
    val userID = user!!.uid
    val rootRef = firebaseDatabase.reference
    val listIdRef = rootRef.child("Users/$userID/")
    listIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (ds in dataSnapshot.children) {
                try {
                    val value = ds.getValue(String::class.java)
                    when (ds.key) {
                        "name" -> User.user.name = value!!
                        "email" -> User.user.email = value!!
                        "phone" -> User.user.phone = value!!
                        "weight" -> User.user.weight = value!!
                        "pressure" -> User.user.pressure = value!!
                        "birthday" -> User.user.birthday = value!!
                    }
                } catch (e: Exception) {
                    println("wrong type")
                }
                scope.cancel()
            }

        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

@Composable
private fun Splash(alpha: Float) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )

        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "logo",
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.None,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = (alpha * 200).dp)
        )
    }
}

@Composable
private fun Onboarding(
    alpha: Float,
    onClickRegister: () -> Unit,
    onClickLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha = alpha),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Привет",
                color = Color.White, fontSize = 36.sp
            )
            Text(
                text = "Наслаждайся отборочным.",
                color = Color.White, fontSize = 22.sp
            )
            Text(
                text = "Будет внимателен.",
                color = Color.White, fontSize = 22.sp
            )
            Text(
                text = "Делай хорошо.",
                color = Color.White, fontSize = 22.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp)
        ) {
            Button(
                modifier = Modifier
                    .padding(25.dp)
                    .fillMaxWidth(),
                onClick = onClickLogin
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "Войти в аккаунт",
                    color = Color.White,
                    fontSize = 22.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Еще нет аккаунта?",
                    color = Color.White,
                    fontSize = 16.sp
                )
                TextButton(
                    onClick = onClickRegister
                )
                {
                    Text(
                        text = "Зарегистрируйтесь",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}