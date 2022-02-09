package com.breaktime.lab3.view.profile

import android.net.Uri
import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState
import com.breaktime.lab3.view.photo.PhotoInfo

class ProfileContract {
    sealed class Event : UiEvent {
        data class OnLoadImageButtonClick(val file: Uri) : Event()
        data class OnOpenImageButtonClick(val photoInfo: PhotoInfo) : Event()
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
        data class Image(val info: PhotoInfo) : ProfileState()
    }

    sealed class Effect : UiEffect {
        data class UpdateList(val data: MutableList<PhotoInfo>) : Effect()
        data class ShowIncorrectDataToast(val message: String) : Effect()
        object ShowWrongParamsToast : Effect()
    }
}