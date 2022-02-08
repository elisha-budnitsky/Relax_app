package com.breaktime.lab3.view.photo

import com.breaktime.lab3.view.base.UiEffect
import com.breaktime.lab3.view.base.UiEvent
import com.breaktime.lab3.view.base.UiState

class PhotoContract {
    sealed class Event : UiEvent {
        data class OnDeleteImageButtonClick(val info: PhotoInfo) : Event()
        object OnCloseImageButtonClick : Event()
    }

    data class State(
        val photoState: PhotoState
    ) : UiState

    sealed class PhotoState {
        object Idle : PhotoState()
        object Delete : PhotoState()
        object Close : PhotoState()
    }

    sealed class Effect : UiEffect {}
}