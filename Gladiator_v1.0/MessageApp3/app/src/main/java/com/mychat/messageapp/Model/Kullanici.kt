package com.mychat.messageapp.Model

data class Kullanici(
    val email: String = "",
    val kadi: String = "",
    val isOnline: String = "Offline",
    val uid: String = "",
    val profilResmiUrl: String? = null

)
