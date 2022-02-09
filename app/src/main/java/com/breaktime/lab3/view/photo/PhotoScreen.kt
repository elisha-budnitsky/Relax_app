package com.breaktime.lab3.view.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun PhotoScreen(navController: NavHostController, info: PhotoInfo) {
    val viewModel = get<PhotoViewModel>()
    initObservable(
        rememberCoroutineScope(),
        viewModel,
        navController
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3839))
    ) {
        Image(
            painter = rememberImagePainter(info.downloadLink),
            contentDescription = "photo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextButton(onClick = {
                viewModel.setEvent(
                    PhotoContract.Event.OnDeleteImageButtonClick(info)
                )
            }) {
                Text(
                    text = "delete",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            TextButton(onClick = {
                viewModel.setEvent(
                    PhotoContract.Event.OnCloseImageButtonClick
                )
            }) {
                Text(
                    text = "close",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

private fun initObservable(
    composableScope: CoroutineScope,
    viewModel: PhotoViewModel,
    navController: NavHostController
) {
    composableScope.launch {
        viewModel.uiState.collect {
            composableScope.ensureActive()
            when (it.photoState) {
                is PhotoContract.PhotoState.Idle -> {
                }
                is PhotoContract.PhotoState.Delete -> {
                    navController.popBackStack()
                    viewModel.clearState()
                    composableScope.cancel()
                }
                is PhotoContract.PhotoState.Close -> {
                    navController.popBackStack()
                    viewModel.clearState()
                    composableScope.cancel()
                }
            }
        }
    }
}