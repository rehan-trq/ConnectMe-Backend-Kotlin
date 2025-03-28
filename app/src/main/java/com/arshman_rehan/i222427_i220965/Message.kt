package com.arshman_rehan.i222427_i220965

data class Message(
    val messageId: String = "",
    val text: String = "",
    val image: String = "", // Base64 string, to be implemented later
    val senderId: String = "",
    val timestamp: Long = 0L,
    var isSeen: Boolean = false, // For Vanish Mode
    val vanish: Boolean = false // Indicates if the message was sent in Vanish Mode
)
