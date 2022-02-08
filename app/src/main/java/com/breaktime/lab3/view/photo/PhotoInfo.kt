package com.breaktime.lab3.view.photo

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoInfo(
    val time: String,
    val link: String,
    val downloadLink: Uri
) : Parcelable
