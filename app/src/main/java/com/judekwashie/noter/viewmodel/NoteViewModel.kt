package com.judekwashie.noter.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.judekwashie.noter.database.NoteDatabase
import com.judekwashie.noter.entity.Note
import com.judekwashie.noter.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: NoteRepository
    var notes: LiveData<List<Note>>
    private lateinit var note: LiveData<Note>

    init {
        val notesDao = NoteDatabase.getDatabase(application).getDao()
        repository = NoteRepository(notesDao)
        notes = repository.getAllNotes()
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNote(note)
    }

    fun getNote(noteId: Int): LiveData<Note>{
        return repository.getNote(noteId)
    }


    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }

    fun deleteAllNote() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllNote()
    }


}