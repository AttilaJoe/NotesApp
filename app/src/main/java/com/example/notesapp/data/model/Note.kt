package com.example.notesapp.data.model

import com.google.firebase.Timestamp

data class Note(
    val id: String? = null,
    val title: String = "",
    val content: String = "",
    val timestamp: Timestamp? = Timestamp.now()
)
