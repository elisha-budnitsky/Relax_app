package com.breaktime.lab3.data

object User {
    val user = Data()

    data class Data(
        var name: String = "", var email: String = "", var phone: String = "",
        var weight: String = "", var pressure: String = "", var birthday: String = ""
    )
}