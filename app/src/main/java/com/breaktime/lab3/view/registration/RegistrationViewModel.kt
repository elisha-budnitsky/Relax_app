package com.breaktime.lab3.view.registration

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.breaktime.lab3.data.User
import com.breaktime.lab3.view.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) :
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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = User(name, email, password)
                firebaseDatabase.getReference("Users").child(auth.currentUser!!.uid)
                    .setValue(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            viewModelScope.launch {
                                setState { copy(registrationState = RegistrationContract.RegistrationState.Success) }
                            }
                        } else {
                            viewModelScope.launch {
                                setEffect { RegistrationContract.Effect.ShowIncorrectDataToast(task.exception!!.message!!) }
                                setState { copy(registrationState = RegistrationContract.RegistrationState.Idle) }
                            }
                        }
                    }
            } else {
                viewModelScope.launch {
                    setEffect { RegistrationContract.Effect.ShowIncorrectDataToast(it.exception!!.message!!) }
                    setState { copy(registrationState = RegistrationContract.RegistrationState.Idle) }
                }
            }
        }
    }

    override fun clearState() {
        setState { copy(registrationState = RegistrationContract.RegistrationState.Idle) }
    }
}