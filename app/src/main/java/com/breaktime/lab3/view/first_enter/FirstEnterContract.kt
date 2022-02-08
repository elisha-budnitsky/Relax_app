package com.breaktime.lab3.view.first_enter

import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState

class FirstEnterContract {
    sealed class Event : UiEvent {
        data class OnFinishButtonClick(
            val phone: String,
            val weight: String,
            val pressure: String,
            val birthday: String
        ) :
            Event()
    }

    data class State(
        val firstEnterState: FirstEnterState
    ) : UiState

    sealed class FirstEnterState {
        object Idle : FirstEnterState()
        object Loading : FirstEnterState()
        object Success : FirstEnterState()
    }

    sealed class Effect : UiEffect {}
}