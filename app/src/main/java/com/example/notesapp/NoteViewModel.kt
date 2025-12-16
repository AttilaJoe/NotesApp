package com.example.notesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.data.model.Note
import com.google.firebase.firestore.FirebaseFirestore

class NoteViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    fun fetchNotes() {
        db.collection("notes").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull {
                it.toObject(Note::class.java)?.copy(id = it.id)
            }
            _notes.value = list
        }
    }

    fun addNote(note: Note) {
        db.collection("notes").add(note)
    }

    fun updateNote(noteId: String, note: Note) {
        db.collection("notes").document(noteId).set(note)
    }

    fun deleteNote(noteId: String) {
        db.collection("notes").document(noteId).delete()
    }
}