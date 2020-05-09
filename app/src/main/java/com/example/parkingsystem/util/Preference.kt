package com.example.parkingsystem.util

import com.chibatching.kotpref.KotprefModel

object User : KotprefModel() {
    var id by intPref(0)
    var phone by stringPref("")
    var carModel by stringPref("")
    var carNumber by stringPref("")
    var name by stringPref("")
    var token by stringPref("")
    var balance by stringPref("")
}

object CurrentParking : KotprefModel() {
    var image by stringPref("")
    var name by stringPref("")
    var description by stringPref("")
}

object Order : KotprefModel() {
    var id by intPref(0)
}