package com.bohunapps.comicslibrary.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bohunapps.comicslibrary.CharacterImage
import com.bohunapps.comicslibrary.Destination
import com.bohunapps.comicslibrary.comicsToString
import com.bohunapps.comicslibrary.viewmodel.CollectionDbViewModel
import com.bohunapps.comicslibrary.viewmodel.LibraryApiViewModel

@Composable
fun CharacterDetailScreen(
    libraryApiViewModel: LibraryApiViewModel,
    collectionDbViewModel: CollectionDbViewModel,
    paddingValues: PaddingValues,
    navController: NavHostController,

    ) {
    val character = libraryApiViewModel.characterDetails.value
    val collection by collectionDbViewModel.collection.collectAsState()
    val inCollection = collection.map { it.apiId }.contains(character?.id)

    if (character == null) {
        navController.navigate(Destination.Library.route) {
            popUpTo(Destination.Library.route)
            launchSingleTop = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        collectionDbViewModel.setCurrentCharacterId(character?.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .padding(paddingValues)
            .verticalScroll(
                rememberScrollState()
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = character?.thumbnail?.path + "." + character?.thumbnail?.extension
        val title = character?.name ?: "No name"
        val comics = character?.comics?.items?.mapNotNull { it.name }?.comicsToString()
        val description = character?.description ?: "No description :("

        CharacterImage(
            url = imageUrl, modifier = Modifier
                .width(200.dp)
                .padding(4.dp)
        )

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding(4.dp)
        )

        Text(
            text = comics!!,
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp,
            modifier = Modifier.padding(4.dp)
        )

        Text(text = description, fontSize = 18.sp, modifier = Modifier.padding(4.dp))

        Button(onClick = {
            if (!inCollection) {
                collectionDbViewModel.addCharacter(character)
            }
        }, modifier = Modifier.padding(bottom = 20.dp)) {
            if (!inCollection) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(text = "Add to collection")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text(text = "Added successfully")
                }
            }

        }
    }
}