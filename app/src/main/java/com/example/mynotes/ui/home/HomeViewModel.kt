package com.example.mynotes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.source.note.NoteModel
import com.example.mynotes.source.note.NoteRepository
import kotlinx.coroutines.launch
import org.koin.dsl.module

val homeViewModel = module {
    factory { HomeViewModel(get()) }
}

class HomeViewModel(
    val repository: NoteRepository
) : ViewModel() {

    // Notes list
    val notes = repository.getAllNotes()

    fun removeNote(noteModel: NoteModel) {
        viewModelScope.launch {
            repository.remove(noteModel)
        }
    }

    fun addNote(noteModel: NoteModel) {
        viewModelScope.launch {
            repository.save(noteModel)
        }
    }

}