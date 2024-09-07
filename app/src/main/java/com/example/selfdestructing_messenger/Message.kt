package com.example.selfdestructing_messenger.models

data class Message(
    var text: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    var senderId: String = ""  // 발신자 ID를 추가
)
