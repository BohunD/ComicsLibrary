package com.bohunapps.comicslibrary.model.db

import kotlinx.coroutines.flow.Flow

class CollectionDbRepoImpl(
    private val characterDao: CharacterDao,
    private val noteDao: NoteDao,
) : CollectionDbRepo {
    override suspend fun getCharactersFomRepo(): Flow<List<DbCharacter>> =
        characterDao.getCharacters()

    override suspend fun getCharacterFomRepo(characterId: Int): Flow<DbCharacter> =
        characterDao.getCharacter(characterId)

    override suspend fun addCharacterToRepo(character: DbCharacter) =
        characterDao.addCharacter(character)

    override suspend fun updateCharacterInRepo(character: DbCharacter) =
        characterDao.updateCharacter(character)

    override suspend fun deleteCharacterFromRepo(character: DbCharacter) =
        characterDao.deleteCharacter(character)

    override suspend fun getAllNotes(): Flow<List<DbNote>> = noteDao.getAllNotes()

    override suspend fun getNotesFromRepo(characterId: Int): Flow<List<DbNote>> =
        noteDao.getNotesByCharacter(characterId)

    override suspend fun addNoteToRepo(note: DbNote) {
        noteDao.addNote(note)
    }

    override suspend fun updateNoteInRepo(note: DbNote) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNoteFromRepo(note: DbNote) {
        noteDao.deleteNote(note)
    }

    override suspend fun deleteAllCharacterNotes(characterId: Int) {
        noteDao.deleteAllCharacterNotes(characterId)
    }
}