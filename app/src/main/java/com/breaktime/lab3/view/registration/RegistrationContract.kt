package com.breaktime.lab3.view.registration

import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState

class RegistrationContract {
    sealed class Event : UiEvent {
        data class OnAuthButtonClick(val name: String, val email: String, val password: String) :
            Event()

        object OnLoginButtonClick : Event()
    }

    data class State(
        val registrationState: RegistrationState
    ) : UiState

    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        object Success : RegistrationState()
        object Login : RegistrationState()
    }

    sealed class Effect : UiEffect {
        data class ShowIncorrectDataToast(val message: String) : Effect()
        object ShowWrongParamsToast : Effect()
    }
}