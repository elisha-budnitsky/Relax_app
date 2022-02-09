package com.breaktime.lab3.view.home

import androidx.lifecycle.viewModelScope
import com.breaktime.lab3.R
import com.breaktime.lab3.data.User
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
import java.text.SimpleDateFormat
import java.util.*


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
            val sign = detectSign()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://sameer-kumar-aztro-v1.p.rapidapi.com/?sign=$sign&day=today")
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

    private fun detectSign(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = sdf.parse(User.birthday)
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        val day = cal.get(Calendar.DATE)
        val month = cal.get(Calendar.MONTH) + 1
        return if (month == 12 && day >= 22 && day <= 31 || month == 1 && day >= 1 && day <= 19) "capricorn"
        else if (month == 1 && day >= 20 && day <= 31 || month == 2 && day >= 1 && day <= 17) "aquarius"
        else if (month == 2 && day >= 18 && day <= 29 || month == 3 && day >= 1 && day <= 19) "pisces"
        else if (month == 3 && day >= 20 && day <= 31 || month == 4 && day >= 1 && day <= 19) "aries"
        else if (month == 4 && day >= 20 && day <= 30 || month == 5 && day >= 1 && day <= 20) "taurus"
        else if (month == 5 && day >= 21 && day <= 31 || month == 6 && day >= 1 && day <= 20) "gemini"
        else if (month == 6 && day >= 21 && day <= 30 || month == 7 && day >= 1 && day <= 22) "cancer"
        else if (month == 7 && day >= 23 && day <= 31 || month == 8 && day >= 1 && day <= 22) "leo"
        else if (month == 8 && day >= 23 && day <= 31 || month == 9 && day >= 1 && day <= 22) "virgo"
        else if (month == 9 && day >= 23 && day <= 30 || month == 10 && day >= 1 && day <= 22) "libra"
        else if (month == 10 && day >= 23 && day <= 31 || month == 11 && day >= 1 && day <= 21) "scorpio"
        else if (month == 11 && day >= 22 && day <= 30 || month == 12 && day >= 1 && day <= 21) "sagittarius"
        else "Illegal date"
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