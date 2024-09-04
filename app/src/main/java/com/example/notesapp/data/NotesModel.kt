package com.example.notesapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
data class NotesModel(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "note_title")
    var noteTitle: String = "",

    @ColumnInfo(name = "note_description")
    var noteDescription: String = "",

    @ColumnInfo(name = "note_date")
    var noteDate: String = ""
) : Serializable