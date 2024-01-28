package com.bohunapps.comicslibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bohunapps.comicslibrary.model.api.MarvelApiRepo
import com.bohunapps.comicslibrary.model.connectivity.ConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryApiViewModel @Inject constructor(
    private val repo: MarvelApiRepo,
    connectivityMonitor: ConnectivityMonitor
): ViewModel() {

    val result = repo.characters
    val queryText = MutableStateFlow("")

    val characterDetails = repo.characterDetails
    private val queryInput = Channel<String>(Channel.CONFLATED)
    val networkAvailable = connectivityMonitor

    init {
        retrieveCharacters()
    }

    @OptIn(FlowPreview::class)
    private fun retrieveCharacters(){
        viewModelScope.launch(Dispatchers.IO) {
            queryInput.receiveAsFlow().filter { validateQuery(it) }
                .debounce(1000)
                .collect{
                    repo.query(it)
                }
        }
    }

    private fun validateQuery(query: String): Boolean= query.length >=2

    fun onQueryUpdate(input: String){
        queryText.value = input
        queryInput.trySend(input)
    }

    fun retrieveSingleCharacter(id: Int){
        repo.getSingleCharacter(id)
    }
}

