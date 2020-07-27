package com.judekwashie.noter.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note( var noteTitle: String, var noteText: String, var noteDate: String){
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0

}