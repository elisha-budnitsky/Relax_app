package com.breaktime.lab3.data

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
    }
}