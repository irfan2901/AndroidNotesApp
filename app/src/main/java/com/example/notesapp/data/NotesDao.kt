package com.example.notesapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    suspend fun insertNote(note: NotesModel)

    @Update
    suspend fun updateNote(note: NotesModel)

    @Delete
    suspend fun deleteNote(note: NotesModel)

    @Query("SELECT * FROM notes_table ORDER BY id DESC")
    fun showAllNotes(): Flow<List<NotesModel>>

}