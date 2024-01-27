package com.bohunapps.comicslibrary

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bohunapps.comicslibrary.ui.theme.ComicsLibraryTheme
import com.bohunapps.comicslibrary.view.CharacterDetailScreen
import com.bohunapps.comicslibrary.view.CharactersBottomNav
import com.bohunapps.comicslibrary.view.CollectionScreen
import com.bohunapps.comicslibrary.view.LibraryScreen
import com.bohunapps.comicslibrary.viewmodel.CollectionDbViewModel
import com.bohunapps.comicslibrary.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String){
    object Library: Destination("library")
    object Collection: Destination("collection")
    object CharacterDetail: Destination("character/{characterId}"){
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val lvm by viewModels<LibraryApiViewModel>()
    private val cvm by viewModels<CollectionDbViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicsLibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CharactersScaffold(navController = navController, lvm, cvm)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScaffold(navController: NavHostController, lvm: LibraryApiViewModel, cvm: CollectionDbViewModel){
    val ctx = LocalContext.current

    Scaffold(
        bottomBar = {
            CharactersBottomNav(navController = navController)
        }
    ) {paddingValues ->
        NavHost(navController = navController, startDestination = Destination.Library.route){
            composable(Destination.Library.route){ LibraryScreen(navController,lvm, paddingValues)}
            composable(Destination.Collection.route){ CollectionScreen() }
            composable(Destination.CharacterDetail.route){ navBackStackEntry->
                val id = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                if(id == null){
                    Toast.makeText(ctx, "Character id is required", Toast.LENGTH_SHORT).show()
                }else{
                    lvm.retrieveSingleCharacter(id)
                    CharacterDetailScreen(lvm, cvm,paddingValues,navController)
                }
            }
        }
    }
}
