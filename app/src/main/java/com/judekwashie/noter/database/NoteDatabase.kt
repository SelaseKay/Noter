package com.judekwashie.noter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.judekwashie.noter.dao.NoteDao
import com.judekwashie.noter.entity.Note

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {


    abstract fun getDao(): NoteDao

    companion object{

        private var INSTANCE: NoteDatabase? = null

        private const val NOTE_DATABASE_NAME = "note_database"

        fun getDatabase(context: Context): NoteDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, NOTE_DATABASE_NAME).build()
                INSTANCE = instance
                return instance
            }
        }
    }


}