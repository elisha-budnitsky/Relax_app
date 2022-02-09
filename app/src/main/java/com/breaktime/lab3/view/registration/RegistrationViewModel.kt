package com.breaktime.lab3.view.registration

import android.util.Patterns
import com.breaktime.lab3.firebase.Firebase
import com.breaktime.lab3.view.base.BaseViewModel

class RegistrationViewModel(private val firebase: Firebase) :
    BaseViewModel<RegistrationContract.Event, RegistrationContract.State, RegistrationContract.Effect>() {
    override fun createInitialState(): RegistrationContract.State {
        return RegistrationContract.State(
            RegistrationContract.RegistrationState.Idle
        )
    }

    override fun handleEvent(event: RegistrationContract.Event) {
        when (event) {
            is RegistrationContract.Event.OnAuthButtonClick -> {
                register(event.name, event.email, event.password)
            }
            is RegistrationContract.Event.OnLoginButtonClick -> {
                setState { copy(registrationState = RegistrationContract.RegistrationState.Login) }
            }
        }
    }

    private fun isFieldsCorrect(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && password.isNotEmpty()
    }

    private fun register(name: String, email: String, password: String) {
        if (!isFieldsCorrect(name, email, password)) {
            setEffect { RegistrationContract.Effect.ShowWrongParamsToast }
            return
        }
        setState { copy(registrationState = RegistrationContract.RegistrationState.Loading) }
        firebase.register(
            name = name,
            email = email,
            password = password,
            onSuccess = {
                setState { copy(registrationState = RegistrationContract.RegistrationState.Success) }
            },
            onError = {
                setEffect { RegistrationContract.Effect.ShowIncorrectDataToast(it) }
                setState { copy(registrationState = RegistrationContract.RegistrationState.Idle) }
            }
        )
    }

    override fun clearState() {
        setState { copy(registrationState = RegistrationContract.RegistrationState.Idle) }
    }
}