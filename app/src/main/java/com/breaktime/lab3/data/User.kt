package com.breaktime.lab3.data

import android.net.Uri

object User {
    var name: String = ""
    var email: String = ""
    var phone: String = ""
    var weight: String = ""
    var pressure: String = ""
    var birthday: String = ""

    fun resetData() {
        name = ""
        email = ""
        phone = ""
        weight = ""
        pressure = ""
        birthday = ""
        icon = null
    }
}

private var _icon: Uri? = null
var User.icon: Uri?
    get() = _icon
    set(value) {
        _icon = value
    }
