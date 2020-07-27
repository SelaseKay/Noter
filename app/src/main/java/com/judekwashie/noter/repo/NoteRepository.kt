package com.judekwashie.noter.repo

import androidx.lifecycle.LiveData
import com.judekwashie.noter.dao.NoteDao
import com.judekwashie.noter.entity.Note

class NoteRepository(private val noteDao: NoteDao) {

    fun getAllNotes() = noteDao.getAllNotes()

    fun getNote(noteId: Int): LiveData<Note> = noteDao.getNote(noteId)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    suspend fun deleteAllNote() = noteDao.deleteAllNotes()

}