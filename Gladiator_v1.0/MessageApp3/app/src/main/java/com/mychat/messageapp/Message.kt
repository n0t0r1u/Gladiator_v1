package com.mychat.messageapp

import com.google.firebase.Timestamp
import java.util.*

data class Message(
    val senderId: String = "",
    var receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)
