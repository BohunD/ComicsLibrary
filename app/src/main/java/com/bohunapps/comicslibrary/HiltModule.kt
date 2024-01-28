package com.bohunapps.comicslibrary

import android.content.Context
import androidx.room.Room
import com.bohunapps.comicslibrary.model.api.ApiService
import com.bohunapps.comicslibrary.model.api.MarvelApiRepo
import com.bohunapps.comicslibrary.model.connectivity.ConnectivityMonitor
import com.bohunapps.comicslibrary.model.db.CharacterDao
import com.bohunapps.comicslibrary.model.db.CollectionDb
import com.bohunapps.comicslibrary.model.db.CollectionDbRepo
import com.bohunapps.comicslibrary.model.db.CollectionDbRepoImpl
import com.bohunapps.comicslibrary.model.db.Constants.DB
import com.bohunapps.comicslibrary.model.db.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Integer.max
import java.util.Stack

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CollectionDb::class.java, DB).build()

    @Provides
    fun provideCharacterDao(collectionDb: CollectionDb) = collectionDb.characterDao()

    @Provides
    fun provideNoteDao(collectionDb: CollectionDb) = collectionDb.notesDao()

    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao, noteDao: NoteDao): CollectionDbRepo =
        CollectionDbRepoImpl(characterDao, noteDao)

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        ConnectivityMonitor.getInstance(context)


}

