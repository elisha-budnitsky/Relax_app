package com.breaktime.lab3.firebase

import android.net.Uri
import com.breaktime.lab3.data.User
import com.breaktime.lab3.view.home.data.Mood
import com.breaktime.lab3.view.photo.PhotoInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class Firebase(
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) {
    fun loadUser() = auth.currentUser

    fun register(
        name: String, email: String, password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                User.name = name
                User.email = email
                val user = User
                firebaseDatabase.getReference("Users").child(auth.currentUser!!.uid)
                    .setValue(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) onSuccess()
                        else onError(task.exception?.message ?: "")
                    }
            } else onError(it.exception?.message ?: "")
        }
    }

    fun login(
        email: String, password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) onSuccess()
            else onError(task.exception?.message ?: "")
        }
    }

    fun resetPassword(
        email: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) onSuccess()
            else onError(task.exception?.message ?: "")
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun loadUserData(onFinishLoading: (Boolean) -> Unit = {}, onError: (String) -> Unit = {}) {
        val user = auth.currentUser
        val userID = user!!.uid
        val rootRef = firebaseDatabase.reference
        val listIdRef = rootRef.child("Users/$userID/")
        listIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    try {
                        val value = ds.getValue(String::class.java)
                        val property = User::class.memberProperties.find { it.name == ds.key }
                        if (property is KMutableProperty<*>) {
                            property.setter.call(User, value!!)
                        }
                    } catch (e: Exception) {
                        onError(e.message ?: "")
                    }
                }
                onFinishLoading(User.birthday.isNotEmpty())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun saveUserIcon(file: Uri, onSuccess: (Uri) -> Unit = {}, onError: (String) -> Unit = {}) {
        val user = auth.currentUser
        val userID = user!!.uid
        val storageRef: StorageReference = firebaseStorage.reference
        val mountainImagesRef =
            storageRef.child("images/users/$userID/icon/${Calendar.getInstance().time.time}")
        mountainImagesRef.putFile(file).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mountainImagesRef.downloadUrl.addOnCompleteListener { loadingTask ->
                    onSuccess(loadingTask.result!!)
                }
            } else onError(task.exception?.message ?: "")
        }
    }

    fun loadUserIcon(onSuccess: (Uri?) -> Unit = {}, onError: (String) -> Unit = {}) {
        val user = auth.currentUser
        val userID = user!!.uid
        val storageRef = firebaseStorage.reference.storage.getReference("images/users/$userID/icon")
        storageRef.listAll().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result!!.items.isEmpty()) {
                    onSuccess(null)
                } else {
                    val lastImage = task.result!!.items.last()
                    lastImage.downloadUrl.addOnCompleteListener {
                        onSuccess(it.result)
                    }
                }
            } else onError(task.exception?.message ?: "")
        }
    }

    fun saveUserData(user: User, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        firebaseDatabase.getReference("Users").child(auth.currentUser!!.uid)
            .setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) onSuccess()
                else onError(task.exception?.message ?: "")
            }
    }

    fun downloadImages(
        onImageDownloaded: (PhotoInfo) -> Unit = {},
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val user = auth.currentUser
        val userID = user!!.uid
        val storageRef = firebaseStorage.reference.storage.getReference("images/users/$userID")
        storageRef.listAll().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result!!.items.forEach {
                    val time = it.name.substring(0, it.name.indexOf("_"))
                    it.downloadUrl.addOnCompleteListener { task ->
                        val info = PhotoInfo(time, it.path, task.result!!)
                        onImageDownloaded(info)
                    }
                }
                onSuccess()
            } else onError(task.exception?.message ?: "")
        }
    }

    fun deleteImage(info: PhotoInfo, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        val storageRef: StorageReference = firebaseStorage.reference
        val mountainImagesRef = storageRef.child(info.link)
        mountainImagesRef.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) onSuccess()
            else onError(task.exception?.message ?: "")
        }
    }

    fun uploadImage(
        file: Uri,
        onSuccess: (PhotoInfo) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val user = auth.currentUser
        val userID = user!!.uid
        val storageRef: StorageReference = firebaseStorage.reference
        val fileName = File(file.path!!)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = sdf.format(Date())
        val mountainImagesRef = storageRef.child("images/users/$userID/${time}_${fileName.name}")
        mountainImagesRef.putFile(file).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mountainImagesRef.downloadUrl.addOnCompleteListener { loadingTask ->
                    val info = PhotoInfo(time, mountainImagesRef.path, loadingTask.result!!)
                    onSuccess(info)
                }
            } else onError(task.exception?.message ?: "")
        }
    }

    fun getDailyData(onCurrentDate: (Mood) -> Unit = {}, onFindDaily: (Mood) -> Unit = {}) {
        val user = auth.currentUser
        val userID = user!!.uid
        val rootRef = firebaseDatabase.reference
        val listIdRef = rootRef.child("Users/$userID/moods/")
        listIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = mutableMapOf(
                    Mood.CALM to 0,
                    Mood.RELAX to 0,
                    Mood.FOCUS to 0,
                    Mood.EXCITED to 0,
                    Mood.FUN to 0,
                    Mood.SADNESS to 0,
                )
                val sdf = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
                val currentDate = sdf.format(Date())
                for (ds in dataSnapshot.children) {
                    val value = ds.getValue(Mood::class.java)!!
                    if (ds.key == currentDate)
                        onCurrentDate(value)
                    map[value] = map[value]?.plus(1)!!
                }
                val maxMood = map.maxByOrNull { it.value }!!
                if (maxMood.value != 0) onFindDaily(maxMood.key)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun saveMood(mood: Mood) {
        val user = auth.currentUser
        val userID = user!!.uid
        val rootRef = firebaseDatabase.reference
        val sdf = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val listIdRef = rootRef.child("Users/$userID/moods/")
        val map: MutableMap<String?, Any> = HashMap()
        map[currentDate] = mood
        listIdRef.updateChildren(map)
    }
}