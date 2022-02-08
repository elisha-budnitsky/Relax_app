package com.breaktime.lab3.view.photo

import com.breaktime.lab3.view.base.BaseViewModel
import com.breaktime.lab3.view.profile.ProfileViewModel

class PhotoViewModel(private val profileViewModel: ProfileViewModel) :
    BaseViewModel<PhotoContract.Event, PhotoContract.State, PhotoContract.Effect>() {
    override fun createInitialState(): PhotoContract.State {
        return PhotoContract.State(
            PhotoContract.PhotoState.Idle
        )
    }

    override fun handleEvent(event: PhotoContract.Event) {
        when (event) {
            is PhotoContract.Event.OnCloseImageButtonClick -> {
                setState { copy(photoState = PhotoContract.PhotoState.Close) }
            }
            is PhotoContract.Event.OnDeleteImageButtonClick -> {
                profileViewModel.deleteImage(event.info)
                setState { copy(photoState = PhotoContract.PhotoState.Delete) }
            }
        }
    }

    override fun clearState() {
        setState { copy(photoState = PhotoContract.PhotoState.Idle) }
    }
}