package com.bohunapps.comicslibrary.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bohunapps.comicslibrary.model.db.Constants.NOTE_TABLE
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Query("SELECT * FROM $NOTE_TABLE ORDER BY id")
    fun getAllNotes(): Flow<List<DbNote>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE characterId = :characterId ORDER BY id ASC")
    fun getNotesByCharacter(characterId: Int): Flow<List<DbNote>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNote(note: DbNote)

    @Update
    fun updateNote(note: DbNote)

    @Delete
    fun deleteNote(note: DbNote)

    @Query("DELETE FROM $NOTE_TABLE WHERE characterId == :characterId")
    fun deleteAllCharacterNotes(characterId: Int)
}