package com.example.mynotes.source.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynotes.source.note.NoteDao
import com.example.mynotes.source.note.NoteModel

@Database(
    entities = [NoteModel::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseClient : RoomDatabase() {
    abstract val noteDao: NoteDao
}