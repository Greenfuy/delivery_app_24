package com.itis.delivery.utils

object Regexes {
    val nameRegex = "(?=.*[a-z]).{5,}\$".toRegex()
    val passwordRegex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\$".toRegex()
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]{2,}".toRegex()
}