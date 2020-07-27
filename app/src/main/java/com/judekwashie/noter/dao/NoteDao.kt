package com.judekwashie.noter.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.judekwashie.noter.entity.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY noteId DESC")
    fun getAllNotes():LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE :noteId = noteId")
    fun getNote(noteId: Int): LiveData<Note>

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()


}