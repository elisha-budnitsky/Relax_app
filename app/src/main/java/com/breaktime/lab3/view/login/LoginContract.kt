package com.breaktime.lab3.view.login

import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState

class LoginContract {
    sealed class Event : UiEvent {
        data class OnAuthButtonClick(val email: String, val password: String) : Event()
        data class OnResetPasswordButtonClick(val email: String) : Event()
        object OnRegisterButtonClick : Event()
    }

    data class State(
        val loginState: LoginState
    ) : UiState

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        object Register : LoginState()
    }

    sealed class Effect : UiEffect {
        data class ShowIncorrectDataToast(val message: String) : Effect()
        object ShowCheckEmailToast : Effect()
        object ShowWrongParamsToast : Effect()
    }
}