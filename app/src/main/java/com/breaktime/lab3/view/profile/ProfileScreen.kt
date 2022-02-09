package com.breaktime.lab3.view.profile

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.breaktime.lab3.R
import com.breaktime.lab3.data.User
import com.breaktime.lab3.data.icon
import com.breaktime.lab3.navigation.Screen
import com.breaktime.lab3.view.photo.PhotoInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalFoundationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val viewModel = get<ProfileViewModel>()
    val context = LocalContext.current
    val isLoadingState = remember { mutableStateOf(false) }
    val data = remember { mutableStateOf(viewModel.uriList) }
    val updateList = remember { mutableStateOf(true) }
    initObservable(
        rememberCoroutineScope(),
        context,
        viewModel,
        isLoadingState,
        data to updateList,
        navController
    )
    Box(modifier = Modifier.fillMaxSize()) {
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
                TextButton(onClick = { viewModel.setEvent(ProfileContract.Event.OnMenuButtonClick) }) {
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
                TextButton(onClick = { viewModel.setEvent(ProfileContract.Event.OnLogoutButtonClick) }) {
                    Text(text = "exit", fontSize = 18.sp, color = Color.White)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = if (User.icon == null) painterResource(R.drawable.no_photo)
                    else rememberImagePainter(User.icon),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(User.name, fontSize = 24.sp, color = Color.White)
                    Text(User.birthday, fontSize = 18.sp, color = Color.White)
                    Text("Weight: ${User.weight}", fontSize = 18.sp, color = Color.White)
                    Text("Pressure: ${User.pressure}", fontSize = 18.sp, color = Color.White)
                    Text("Phone: ${User.phone}", fontSize = 18.sp, color = Color.White)
                    Text("Email: ${User.email}", fontSize = 18.sp, color = Color.White)
                }
            }

            val pickPictureLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { imageUri ->
                if (imageUri != null) {
                    viewModel.setEvent(ProfileContract.Event.OnLoadImageButtonClick(imageUri))
                }
            }
            if (updateList.value)
                LazyVerticalGrid(
                    modifier = Modifier.padding(bottom = 50.dp),
                    cells = GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    data.value.forEach { PhotoInfo ->
                        item {
                            TextButton(
                                onClick = {
                                    viewModel.setEvent(
                                        ProfileContract.Event.OnOpenImageButtonClick(PhotoInfo)
                                    )
                                },
                                contentPadding = PaddingValues()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .padding(7.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    backgroundColor = Color.LightGray
                                ) {
                                    Image(
                                        painter = rememberImagePainter(PhotoInfo.downloadLink),
                                        contentDescription = "photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Text(
                                        text = PhotoInfo.time,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Left,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 10.dp, top = 90.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(7.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7AAD77)),
                            onClick = { pickPictureLauncher.launch("image/*") }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "+", fontSize = 40.sp, color = Color.White)
                            }
                        }
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
    viewModel: ProfileViewModel,
    isLoadingState: MutableState<Boolean>,
    dataWithUpdater: Pair<MutableState<MutableList<PhotoInfo>>, MutableState<Boolean>>,
    navController: NavHostController
) {
    composableScope.launch {
        viewModel.uiState.collect {
            composableScope.ensureActive()
            when (it.profileState) {
                is ProfileContract.ProfileState.Logout -> {
                    navController.popBackStack()
                    navController.navigate(Screen.Splash.route)
                    User.resetData()
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is ProfileContract.ProfileState.Menu -> {
                    navController.navigate(Screen.Menu.route)
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is ProfileContract.ProfileState.Image -> {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "info", it.profileState.info
                    )
                    navController.navigate(Screen.Photo.route)
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is ProfileContract.ProfileState.Idle -> {
                    isLoadingState.value = false
                }
                is ProfileContract.ProfileState.Loading -> {
                    isLoadingState.value = true
                }
            }
        }
    }
    composableScope.launch {
        viewModel.effect.collect {
            composableScope.ensureActive()
            when (it) {
                is ProfileContract.Effect.UpdateList -> {
                    val (data, updater) = dataWithUpdater
                    updater.value = false
                    data.value = it.data
                    updater.value = true

                }
                is ProfileContract.Effect.ShowIncorrectDataToast -> {
                    Toast.makeText(
                        context, it.message, Toast.LENGTH_SHORT
                    ).show()
                }
                is ProfileContract.Effect.ShowWrongParamsToast -> {
                    Toast.makeText(
                        context, "Incorrect fields data", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}