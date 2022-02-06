package com.breaktime.lab3.view.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.breaktime.lab3.view.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: FirebaseAuth
) :
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
        }
    }

    private fun isFieldsCorrect(email: String, password: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && password.isNotEmpty()
    }

    private fun login(email: String, password: String) {
        if (!isFieldsCorrect(email, password)) {
            setEffect { LoginContract.Effect.ShowWrongParamsToast }
            return
        }
        setState { copy(loginState = LoginContract.LoginState.Loading) }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                setState { copy(loginState = LoginContract.LoginState.Success) }
            } else {
                viewModelScope.launch {
                    setEffect { LoginContract.Effect.ShowIncorrectDataToast(it.exception!!.message!!) }
                    setState { copy(loginState = LoginContract.LoginState.Idle) }
                }
            }
        }
    }

    override fun clearState() {
        setState { copy(loginState = LoginContract.LoginState.Idle) }
    }
}