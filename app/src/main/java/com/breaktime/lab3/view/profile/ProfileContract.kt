package com.breaktime.lab3.view.profile

import android.net.Uri
import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState

class ProfileContract {
    sealed class Event : UiEvent {
        data class OnLoadImageButtonClick(val file: Uri) : Event()
        data class OnOpenImageButtonClick(val link: Uri) : Event()
        object OnLogoutButtonClick : Event()
        object OnMenuButtonClick : Event()
    }

    data class State(
        val profileState: ProfileState
    ) : UiState

    sealed class ProfileState {
        object Idle : ProfileState()
        object Loading : ProfileState()
        object Logout : ProfileState()
        object Menu : ProfileState()
        data class Image(val link: Uri) : ProfileState()
    }

    sealed class Effect : UiEffect {
        data class UpdateList(val data: List<Pair<String, Uri>>) : Effect()
        data class ShowIncorrectDataToast(val message: String) : Effect()
        object ShowWrongParamsToast : Effect()
    }
}