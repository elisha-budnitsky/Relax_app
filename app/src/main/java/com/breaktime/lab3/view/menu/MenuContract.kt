package com.breaktime.lab3.view.menu

import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState

class MenuContract {
    sealed class Event : UiEvent {
        object OnEditProfileButtonClick : Event()
        object OnCalculateBmiButtonClick : Event()
        object OnAboutDeveloperButtonClick : Event()
        object OnGuideButtonClick : Event()
    }

    data class State(
        val menuState: MenuState
    ) : UiState

    sealed class MenuState {
        object Idle : MenuState()
        object EditProfile : MenuState()
        object CalculateBmi : MenuState()
        object AboutDeveloper : MenuState()
        object Guide : MenuState()
    }

    sealed class Effect : UiEffect
}