package com.breaktime.lab3.view.profile

import android.net.Uri
import com.breaktime.lab3.view.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileViewModel(
    private val auth: FirebaseAuth
) :
    BaseViewModel<ProfileContract.Event, ProfileContract.State, ProfileContract.Effect>() {
    val uriList = mutableListOf<Pair<String, Uri>>()

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
                setState { copy(profileState = ProfileContract.ProfileState.Image(event.link)) }
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
        auth.signOut()
    }

    private fun uploadImage(file: Uri) {
        setState { copy(profileState = ProfileContract.ProfileState.Loading) }
        val user = auth.currentUser
        val userID = user!!.uid
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val fileName = File(file.path)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = sdf.format(Date())
        val mountainImagesRef = storageRef.child("images/users/$userID/${time}_${fileName.name}")
        mountainImagesRef.putFile(file).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mountainImagesRef.downloadUrl.addOnCompleteListener { loadingTask ->
                    uriList.add(time to loadingTask.result!!)
                    setEffect { ProfileContract.Effect.UpdateList(uriList) }
                }
            } else {
                println("error " + task.exception?.message)
                setEffect { ProfileContract.Effect.ShowIncorrectDataToast(task.exception!!.message!!) }
            }
            setState { copy(profileState = ProfileContract.ProfileState.Idle) }
        }
    }

    private fun downloadImages() {
        val user = auth.currentUser
        val userID = user!!.uid
        val storageRef =
            FirebaseStorage.getInstance().reference.storage.getReference("images/users/$userID")
        storageRef.listAll().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                setEffect { ProfileContract.Effect.ShowIncorrectDataToast(task.exception!!.message!!) }
            }
            task.result!!.items.forEach {
                println(it.name)
                val time = it.name.substring(0, it.name.indexOf("_"))
                it.downloadUrl.addOnCompleteListener { task ->
                    uriList.add(time to task.result!!)
                    setEffect { ProfileContract.Effect.UpdateList(uriList) }
                }
            }
            setState { copy(profileState = ProfileContract.ProfileState.Idle) }
        }
    }

    override fun clearState() {
        setState { copy(profileState = ProfileContract.ProfileState.Idle) }
    }
}