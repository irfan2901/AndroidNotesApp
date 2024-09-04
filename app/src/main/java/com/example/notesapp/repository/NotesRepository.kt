package com.example.notesapp.repository

import com.example.notesapp.data.NotesModel
import com.example.notesapp.database.NotesDatabase
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val database: NotesDatabase) {

    fun showAllNotes(): Flow<List<NotesModel>> = database.notesDao().showAllNotes()

    suspend fun insertNote(note: NotesModel) {
        database.notesDao().insertNote(note)
    }

    suspend fun updateNote(note: NotesModel) {
        database.notesDao().updateNote(note)
    }

    suspend fun deleteNote(note: NotesModel) {
        database.notesDao().deleteNote(note)
    }

}