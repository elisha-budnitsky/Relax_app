package com.breaktime.lab3.view.first_enter

import com.breaktime.lab3.data.User
import com.breaktime.lab3.firebase.Firebase
import com.breaktime.lab3.view.base.BaseViewModel

class FirstEnterViewModel(private val firebase: Firebase) :
    BaseViewModel<FirstEnterContract.Event, FirstEnterContract.State, FirstEnterContract.Effect>() {
    override fun createInitialState(): FirstEnterContract.State {
        return FirstEnterContract.State(
            FirstEnterContract.FirstEnterState.Idle
        )
    }

    override fun handleEvent(event: FirstEnterContract.Event) {
        when (event) {
            is FirstEnterContract.Event.OnFinishButtonClick -> {
                saveData(event.phone, event.weight, event.pressure, event.birthday)
            }
        }
    }

    private fun saveData(phone: String, weight: String, pressure: String, birthday: String) {
        setState { copy(firstEnterState = FirstEnterContract.FirstEnterState.Loading) }
        User.phone = phone
        User.weight = weight
        User.pressure = pressure
        User.birthday = birthday
        firebase.saveUserData(
            user = User,
            onSuccess = {
                setState { copy(firstEnterState = FirstEnterContract.FirstEnterState.Success) }
            })
    }

    override fun clearState() {
        setState { copy(firstEnterState = FirstEnterContract.FirstEnterState.Idle) }
    }
}