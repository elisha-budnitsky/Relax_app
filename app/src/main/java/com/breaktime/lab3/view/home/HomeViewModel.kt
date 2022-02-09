package com.breaktime.lab3.view.home

import androidx.lifecycle.viewModelScope
import com.breaktime.lab3.R
import com.breaktime.lab3.data.dailySuggestions
import com.breaktime.lab3.data.todaySuggestions
import com.breaktime.lab3.firebase.Firebase
import com.breaktime.lab3.view.base.BaseViewModel
import com.breaktime.lab3.view.home.data.HoroscopeData
import com.breaktime.lab3.view.home.data.Mood
import com.breaktime.lab3.view.home.data.MoodData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class HomeViewModel(private val firebase: Firebase) :
    BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {
    var horoscopeData: HoroscopeData? = null
    var dailyData: MoodData? = null
    var todayData: MoodData? = null

    val moodData = listOf(
        MoodInfo("Calm", R.drawable.ic_calm, false, Mood.CALM),
        MoodInfo("Relax", R.drawable.ic_relax, false, Mood.RELAX),
        MoodInfo("Focus", R.drawable.ic_focus, false, Mood.FOCUS),
        MoodInfo("Excited", R.drawable.ic_excited, false, Mood.EXCITED),
        MoodInfo("Fun", R.drawable.ic_fun, false, Mood.FUN),
        MoodInfo("Sadness", R.drawable.ic_sadness, false, Mood.SADNESS)
    )

    init {
        getHoroscopeData()
        getDailyData()
    }

    private fun setActiveMood(mood: Mood) {
        moodData.onEach { it.isSelected = false }.first { it.mood == mood }.isSelected = true
        setEffect { HomeContract.Effect.UpdateMoodList(moodData) }
    }

    override fun createInitialState(): HomeContract.State {
        return HomeContract.State(
            HomeContract.HomeState.Idle
        )
    }

    override fun handleEvent(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.OnMenuButtonClick -> {
                setState { copy(homeState = HomeContract.HomeState.Menu) }
            }
            is HomeContract.Event.OnMoodButtonClick -> {
                getTodayData(event.mood)
                setActiveMood(event.mood)
            }
        }
    }

    private fun getHoroscopeData() {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://sameer-kumar-aztro-v1.p.rapidapi.com/?sign=aquarius&day=today")
                .post(RequestBody.create(null, ByteArray(0)))
                .addHeader("x-rapidapi-host", "sameer-kumar-aztro-v1.p.rapidapi.com")
                .addHeader(
                    "x-rapidapi-key",
                    "f382cecaf5msh20615aae7f2daedp15743ajsnded8ea2e90bb"
                )
                .build()
            client.newCall(request).execute().use { response ->
                horoscopeData =
                    Gson().fromJson(response.body()!!.string(), HoroscopeData::class.java)
                setEffect {
                    HomeContract.Effect.UpdateSuggestionList(
                        horoscopeData,
                        todayData,
                        dailyData
                    )
                }
            }
        }
    }

    private fun buildDailyData(ofterMood: Mood?): MoodData {
        return MoodData(dailySuggestions[ofterMood]!!)
    }

    private fun getTodayData(mood: Mood) {
        saveMood(mood)
        todayData = MoodData(todaySuggestions[mood]!!)
        setEffect {
            HomeContract.Effect.UpdateSuggestionList(
                horoscopeData, todayData, dailyData
            )
        }
    }

    private fun getDailyData() {
        firebase.getDailyData(
            onCurrentDate = { mood ->
                getTodayData(mood)
                setActiveMood(mood)
            },
            onFindDaily = { mood ->
                dailyData = buildDailyData(mood)
                setEffect {
                    HomeContract.Effect.UpdateSuggestionList(
                        horoscopeData,
                        todayData,
                        dailyData
                    )
                }
            }
        )
    }

    private fun saveMood(mood: Mood) {
        firebase.saveMood(mood = mood)
    }

    override fun clearState() {
        setState { copy(homeState = HomeContract.HomeState.Idle) }
    }
}