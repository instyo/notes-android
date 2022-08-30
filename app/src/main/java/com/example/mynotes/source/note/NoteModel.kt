package com.example.mynotes.source.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
data class NoteModel(
    val title: String,
    val description: String,
    val pinned: Boolean = false,
    // default is white
    val color: String = "#ffffff",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : Serializable
