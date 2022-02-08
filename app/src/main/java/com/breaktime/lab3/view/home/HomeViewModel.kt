package com.breaktime.lab3.view.home

import androidx.lifecycle.viewModelScope
import com.breaktime.lab3.R
import com.breaktime.lab3.view.base.BaseViewModel
import com.breaktime.lab3.view.home.data.HoroscopeData
import com.breaktime.lab3.view.home.data.Mood
import com.breaktime.lab3.view.home.data.MoodData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*


class HomeViewModel(
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) :
    BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {
    var horoscopeData: HoroscopeData? = null
    var dailyData: MoodData? = null
    var todayData: MoodData? = null

    val moodData = listOf(
        MoodInfo("Спокойным", R.drawable.ic_calm, false, Mood.CALM),
        MoodInfo("Расслабленным", R.drawable.ic_relax, false, Mood.RELAX),
        MoodInfo("Сосредоточенным", R.drawable.ic_focus, false, Mood.FOCUS),
        MoodInfo("Взволнованным", R.drawable.ic_excited, false, Mood.EXCITED),
        MoodInfo("Веселым", R.drawable.ic_fun, false, Mood.FUN),
        MoodInfo("Грустным", R.drawable.ic_sadness, false, Mood.SADNESS)
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

    private fun getDailyData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = auth.currentUser
            val userID = user!!.uid
            val rootRef = firebaseDatabase.reference
            val listIdRef = rootRef.child("Users/$userID/moods/")
            listIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map = mutableMapOf(
                        Mood.CALM to 0,
                        Mood.RELAX to 0,
                        Mood.FOCUS to 0,
                        Mood.EXCITED to 0,
                        Mood.FUN to 0,
                        Mood.SADNESS to 0,
                    )
                    val sdf = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
                    val currentDate = sdf.format(Date())
                    for (ds in dataSnapshot.children) {
                        val value = ds.getValue(Mood::class.java)!!
                        if (ds.key == currentDate) {
                            getTodayData(value)
                            setActiveMood(value)
                        }
                        map[value] = map[value]?.plus(1)!!
                    }
                    val ofterMood = map.maxByOrNull { it.value }?.key
                    dailyData = buildDailyData(ofterMood)
                    setEffect {
                        HomeContract.Effect.UpdateSuggestionList(
                            horoscopeData,
                            todayData,
                            dailyData
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun buildDailyData(ofterMood: Mood?): MoodData {
        return MoodData("daily")
    }

    private fun getTodayData(mood: Mood) {
        saveMood(mood)
        todayData = MoodData("today")
        setEffect {
            HomeContract.Effect.UpdateSuggestionList(
                horoscopeData, todayData, dailyData
            )
        }
    }

    private fun saveMood(mood: Mood) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = auth.currentUser
            val userID = user!!.uid
            val rootRef = firebaseDatabase.reference
            val sdf = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
            val currentDate = sdf.format(Date())
            val listIdRef = rootRef.child("Users/$userID/moods/")
            val map: MutableMap<String?, Any> = HashMap()
            map[currentDate] = mood
            listIdRef.updateChildren(map)
        }
    }

    override fun clearState() {
        setState { copy(homeState = HomeContract.HomeState.Idle) }
    }
}