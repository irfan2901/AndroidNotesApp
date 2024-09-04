package com.example.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.NotesModel
import com.example.notesapp.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _notes = MutableStateFlow<List<NotesModel>>(emptyList())
    val notes: StateFlow<List<NotesModel>> get() = _notes

    init {
        viewModelScope.launch {
            repository.showAllNotes().collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    fun insertNote(note: NotesModel) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun updateNote(note: NotesModel) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun deleteNote(note: NotesModel) = viewModelScope.launch {
        repository.deleteNote(note)
    }

}