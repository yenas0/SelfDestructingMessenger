package com.example.selfdestructing_messenger.models

data class Message(
    var text: String = "",
    var timestamp: Long = System.currentTimeMillis()
)
