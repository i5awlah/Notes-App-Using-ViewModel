package com.example.notesappviewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*


class NoteViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NoteRepository
    private val notes: LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        notes = repository.getNotes
    }

    fun getNote(): LiveData<List<Note>> {
        return notes
    }

    fun addNote(noteContent: String) {
        if (noteContent.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.addNote(Note(0, noteContent))
            }
        } else {
            Log.d("Main", "empty!")
        }
    }

    fun editNote(noteID: Int, noteContent: String){
        if (noteContent.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.updateNote(Note(noteID, noteContent))
            }
        } else {
            Log.d("Main", "empty!")
        }
    }

    fun deleteNote(noteID: Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteNote(Note(noteID,""))
        }
    }



}