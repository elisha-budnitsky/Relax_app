package com.breaktime.lab3.view.first_enter

import androidx.lifecycle.viewModelScope
import com.breaktime.lab3.data.User
import com.breaktime.lab3.view.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class FirstEnterViewModel(
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) :
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
        User.user.phone = phone
        User.user.weight = weight
        User.user.pressure = pressure
        User.user.birthday = birthday
        val user = User.user
        firebaseDatabase.getReference("Users").child(auth.currentUser!!.uid)
            .setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        setState { copy(firstEnterState = FirstEnterContract.FirstEnterState.Success) }
                    }
                } else {
                    viewModelScope.launch {
                        setState { copy(firstEnterState = FirstEnterContract.FirstEnterState.Idle) }
                    }
                }
            }
    }

    override fun clearState() {
        setState { copy(firstEnterState = FirstEnterContract.FirstEnterState.Idle) }
    }
}