package com.bohunapps.comicslibrary.model.db

import kotlinx.coroutines.flow.Flow

interface CollectionDbRepo {
    suspend fun getCharactersFomRepo(): Flow<List<DbCharacter>>
    suspend fun getCharacterFomRepo(characterId: Int): Flow<DbCharacter>
    suspend fun addCharacterToRepo(character: DbCharacter)
    suspend fun updateCharacterInRepo(character: DbCharacter)
    suspend fun deleteCharacterFromRepo(character: DbCharacter)

}