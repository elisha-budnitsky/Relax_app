package com.breaktime.lab3.view.home

import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState
import com.breaktime.lab3.view.home.data.HoroscopeData
import com.breaktime.lab3.view.home.data.Mood
import com.breaktime.lab3.view.home.data.MoodData

class HomeContract {
    sealed class Event : UiEvent {
        object OnMenuButtonClick : Event()
        data class OnMoodButtonClick(val mood: Mood) : Event()
    }

    data class State(
        val homeState: HomeState
    ) : UiState

    sealed class HomeState {
        object Idle : HomeState()
        object Menu : HomeState()
    }

    sealed class Effect : UiEffect {
        data class UpdateSuggestionList(
            val horoscopeData: HoroscopeData?,
            val todayData: MoodData?,
            val dailyData: MoodData?
        ) : Effect()

        data class UpdateMoodList(
            val list: List<MoodInfo>
        ) : Effect()
    }
}