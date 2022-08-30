package com.example.mynotes.source.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mynotes.source.note.NoteModel

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table ORDER BY  pinned DESC, updatedAt DESC")
    fun getAllNotes(): LiveData<List<NoteModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteModel)

    @Update
    suspend fun update(note: NoteModel)

    @Delete
    suspend fun delete(note: NoteModel)

    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery ORDER BY pinned DESC, updatedAt DESC")
    fun searchNotes(searchQuery: String): LiveData<List<NoteModel>>
}