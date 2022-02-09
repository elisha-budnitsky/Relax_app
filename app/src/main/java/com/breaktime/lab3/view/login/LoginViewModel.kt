package com.breaktime.lab3.view.login

import android.util.Patterns
import com.breaktime.lab3.firebase.Firebase
import com.breaktime.lab3.view.base.BaseViewModel

class LoginViewModel(private val firebase: Firebase) :
    BaseViewModel<LoginContract.Event, LoginContract.State, LoginContract.Effect>() {
    override fun createInitialState(): LoginContract.State {
        return LoginContract.State(
            LoginContract.LoginState.Idle
        )
    }

    override fun handleEvent(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.OnAuthButtonClick -> {
                login(event.email, event.password)
            }
            is LoginContract.Event.OnRegisterButtonClick -> {
                setState { copy(loginState = LoginContract.LoginState.Register) }
            }
            is LoginContract.Event.OnResetPasswordButtonClick -> {
                resetPassword(event.email)
            }
        }
    }

    private fun isEmailCorrect(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordCorrect(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun login(email: String, password: String) {
        if (!(isEmailCorrect(email) && isPasswordCorrect(password))) {
            setEffect { LoginContract.Effect.ShowWrongParamsToast }
            return
        }
        setState { copy(loginState = LoginContract.LoginState.Loading) }
        firebase.login(
            email = email,
            password = password,
            onSuccess = {
                firebase.loadUserData(onFinishLoading = { isUserDataSet ->
                    if (isUserDataSet)
                        setState { copy(loginState = LoginContract.LoginState.Menu) }
                    else setState { copy(loginState = LoginContract.LoginState.FirstEnter) }
                })
            }, onError = {
                setEffect { LoginContract.Effect.ShowIncorrectDataToast(it) }
                setState { copy(loginState = LoginContract.LoginState.Idle) }
            }
        )
    }

    private fun resetPassword(email: String) {
        if (!isEmailCorrect(email)) {
            setEffect { LoginContract.Effect.ShowWrongParamsToast }
            return
        }
        setState { copy(loginState = LoginContract.LoginState.Loading) }
        firebase.resetPassword(
            email = email,
            onSuccess = {
                setEffect { LoginContract.Effect.ShowCheckEmailToast }
            },
            onError = {
                setEffect { LoginContract.Effect.ShowIncorrectDataToast(it) }
            }
        )
        setState { copy(loginState = LoginContract.LoginState.Idle) }
    }

    override fun clearState() {
        setState { copy(loginState = LoginContract.LoginState.Idle) }
    }
}