package com.arshman_rehan.i222427_i220965

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class StoryInfo(
    val storyId: String? = null,        // Unique ID for the story
    val bitmapString: String? = null,   // Base64-encoded image string
    val timestamp: Long? = null         // When the story was created
) {
    // Default constructor for Firebase deserialization
    constructor() : this(null, null, null)
}