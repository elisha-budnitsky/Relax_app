package com.breaktime.lab3.view.profile

import android.net.Uri
import com.breaktime.lab3.firebase.Firebase
import com.breaktime.lab3.view.base.BaseViewModel
import com.breaktime.lab3.view.photo.PhotoInfo

class ProfileViewModel(private val firebase: Firebase) :
    BaseViewModel<ProfileContract.Event, ProfileContract.State, ProfileContract.Effect>() {
    val uriList = mutableListOf<PhotoInfo>()

    init {
        downloadImages()
    }

    override fun createInitialState(): ProfileContract.State {
        return ProfileContract.State(
            ProfileContract.ProfileState.Idle
        )
    }

    override fun handleEvent(event: ProfileContract.Event) {
        when (event) {
            is ProfileContract.Event.OnLoadImageButtonClick -> {
                uploadImage(event.file)
            }
            is ProfileContract.Event.OnOpenImageButtonClick -> {
                setState { copy(profileState = ProfileContract.ProfileState.Image(event.photoInfo)) }
            }
            is ProfileContract.Event.OnLogoutButtonClick -> {
                logout()
                setState { copy(profileState = ProfileContract.ProfileState.Logout) }
            }
            is ProfileContract.Event.OnMenuButtonClick -> {
                setState { copy(profileState = ProfileContract.ProfileState.Menu) }
            }
        }
    }

    private fun logout() {
        firebase.logout()
    }

    private fun uploadImage(file: Uri) {
        setState { copy(profileState = ProfileContract.ProfileState.Loading) }
        firebase.uploadImage(
            file = file,
            onSuccess = {
                uriList.add(it)
                setEffect { ProfileContract.Effect.UpdateList(uriList) }
                setState { copy(profileState = ProfileContract.ProfileState.Idle) }
            }, onError = {
                setEffect { ProfileContract.Effect.ShowIncorrectDataToast(it) }
                setState { copy(profileState = ProfileContract.ProfileState.Idle) }
            }
        )
    }

    private fun downloadImages() {
        setState { copy(profileState = ProfileContract.ProfileState.Loading) }
        firebase.downloadImages(
            onImageDownloaded = {
                uriList.add(it)
                setEffect { ProfileContract.Effect.UpdateList(uriList) }
            },
            onSuccess = {
                setState { copy(profileState = ProfileContract.ProfileState.Idle) }
            },
            onError = {
                setEffect { ProfileContract.Effect.ShowIncorrectDataToast(it) }
                setState { copy(profileState = ProfileContract.ProfileState.Idle) }
            }
        )
    }

    fun deleteImage(info: PhotoInfo) {
        firebase.deleteImage(
            info = info,
            onSuccess = {
                uriList.remove(info)
                setEffect { ProfileContract.Effect.UpdateList(uriList) }
            },
            onError = {
                setEffect { ProfileContract.Effect.ShowIncorrectDataToast(it) }
            }
        )
    }

    override fun clearState() {
        setState { copy(profileState = ProfileContract.ProfileState.Idle) }
    }
}