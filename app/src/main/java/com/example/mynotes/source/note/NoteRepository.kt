package com.example.mynotes.source.note

import androidx.lifecycle.LiveData
import org.koin.dsl.module

val noteRepositoryModule = module {
    factory { NoteRepository(get()) }
}

class NoteRepository(
    private val db: NoteDao
) {

    fun getAllNotes() : LiveData<List<NoteModel>> {
        return db.getAllNotes()
    }

    fun searchNotes(searchQuery: String) : LiveData<List<NoteModel>> {
        return db.searchNotes(searchQuery)
    }

    suspend fun save(noteModel: NoteModel) {
        db.insert(noteModel)
    }

    suspend fun remove(noteModel: NoteModel) {
        db.delete(noteModel)
    }

    suspend fun update(noteModel: NoteModel) {
        db.update(noteModel)
    }

}