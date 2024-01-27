package com.bohunapps.comicslibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bohunapps.comicslibrary.model.CharacterResult
import com.bohunapps.comicslibrary.model.db.CollectionDbRepo
import com.bohunapps.comicslibrary.model.db.DbCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDbViewModel @Inject constructor(private val repo: CollectionDbRepo): ViewModel() {
    val currentCharacter = MutableStateFlow<DbCharacter?>(null)
    val collection = MutableStateFlow<List<DbCharacter>>(listOf())

    init {
        getCollection()
    }

    private fun getCollection(){
        viewModelScope.launch {
            repo.getCharactersFomRepo().collect{
                collection.value = it
            }
        }
    }

    fun setCurrentCharacterId(id: Int?){
        id?.let {
            viewModelScope.launch{
                repo.getCharacterFomRepo(it).collect{
                    currentCharacter.value = it
                }
            }
        }
    }

    fun addCharacter(character: CharacterResult){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addCharacterToRepo(DbCharacter.fromCharacter(character))
        }
    }

    fun deleteCharacter(character: DbCharacter){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteCharacterFromRepo(character)
        }
    }
}