package com.example.mynotes.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.source.note.NoteModel
import com.example.mynotes.source.note.NoteRepository
import kotlinx.coroutines.launch
import org.koin.dsl.module

val formViewModel = module {
    factory { FormViewModel(get()) }
}

class FormViewModel(
    val repository: NoteRepository
) : ViewModel() {

    // Note Form
    var backgroundColor = "#ffffff"
    var pinned = false

    fun saveNote(
        title: String,
        description: String
    ) {
        val note = NoteModel(
            title,
            description,
            pinned,
            backgroundColor
        )

        viewModelScope.launch {
            repository.save(note)
        }
    }

    fun updateNote(
        note: NoteModel
    ) {
        viewModelScope.launch {
            repository.update(note)
        }
    }
}