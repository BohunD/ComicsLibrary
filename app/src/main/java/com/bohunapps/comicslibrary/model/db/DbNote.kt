package com.bohunapps.comicslibrary.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bohunapps.comicslibrary.model.Note
import com.bohunapps.comicslibrary.model.db.Constants.NOTE_TABLE

@Entity(tableName = NOTE_TABLE)
data class DbNote (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val characterId: Int,
    val title: String,
    val text: String
        ){
    companion object{
        fun fromNote(note: Note) = DbNote(id = 0, note.characterId, note.title, note.text)
    }
}