package com.breaktime.lab3.view.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.breaktime.lab3.R
import com.breaktime.lab3.data.User
import com.breaktime.lab3.navigation.Screen
import com.breaktime.lab3.view.home.data.HoroscopeData
import com.breaktime.lab3.view.home.data.MoodData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel = get<HomeViewModel>()
    val context = LocalContext.current
    val updateList = remember { mutableStateOf(true) }
    val updateMood = remember { mutableStateOf(true) }
    val recommendationData = remember {
        mutableStateOf(
            Triple(viewModel.horoscopeData, viewModel.todayData, viewModel.dailyData)
        )
    }
    val moodList = remember { mutableStateOf(viewModel.moodData) }
    initObservable(
        rememberCoroutineScope(),
        context,
        viewModel,
        updateList,
        updateMood,
        recommendationData,
        moodList,
        navController
    )
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
            TextButton(onClick = { viewModel.setEvent(HomeContract.Event.OnMenuButtonClick) }) {
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
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = "avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(43.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )
        }
        Text(
            text = "Welcome back, ${User.user.name}!",
            fontSize = 32.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Text(
            text = "Каким ты себя ощущаешь сегодня?",
            fontSize = 20.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )

        if (updateMood.value) {
            LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
                moodList.value.forEach {
                    item {
                        MoodItem(moodInfo = it, viewModel = viewModel)
                    }
                }
            }
        }

        val (horoscopeData, todayData, dailyData) = recommendationData.value
        val suggestionData = mutableListOf<SuggestionInfo>()
        if (horoscopeData != null)
            suggestionData.add(
                SuggestionInfo(
                    "Horoscope",
                    "Description bla bla bla bla bla",
                    horoscopeData.description,
                    R.drawable.current_recommendation
                )
            )

        if (todayData != null)
            suggestionData.add(
                SuggestionInfo(
                    "Current recommendation",
                    "Description bla bla bla bla bla",
                    todayData.content,
                    R.drawable.daily_recommendation
                )
            )
        if (dailyData != null)
            suggestionData.add(
                SuggestionInfo(
                    "Daily recommendation",
                    "Description bla bla bla bla bla",
                    dailyData.content,
                    R.drawable.horoscope
                )
            )
        if (updateList.value)
            LazyColumn(modifier = Modifier.padding(bottom = 60.dp)) {
                suggestionData.forEach {
                    item {
                        ExpandableItem(it)
                    }
                }
            }
    }
}

private fun initObservable(
    composableScope: CoroutineScope,
    context: Context,
    viewModel: HomeViewModel,
    updater: MutableState<Boolean>,
    updateMood: MutableState<Boolean>,
    suggestionData: MutableState<Triple<HoroscopeData?, MoodData?, MoodData?>>,
    moodList: MutableState<List<MoodInfo>>,
    navController: NavHostController
) {
    composableScope.launch {
        viewModel.uiState.collect {
            composableScope.ensureActive()
            when (it.homeState) {
                is HomeContract.HomeState.Idle -> {}
                is HomeContract.HomeState.Menu -> {
                    navController.navigate(Screen.Menu.route)
                    viewModel.clearState()
                    composableScope.cancel()
                }
            }
        }
    }
    composableScope.launch {
        viewModel.effect.collect {
            composableScope.ensureActive()
            when (it) {
                is HomeContract.Effect.UpdateSuggestionList -> {
                    updater.value = false
                    suggestionData.value = Triple(it.horoscopeData, it.todayData, it.dailyData)
                    updater.value = true
                }
                is HomeContract.Effect.UpdateMoodList -> {
                    updateMood.value = false
                    moodList.value = it.list
                    updateMood.value = true
                }
            }
        }
    }
}