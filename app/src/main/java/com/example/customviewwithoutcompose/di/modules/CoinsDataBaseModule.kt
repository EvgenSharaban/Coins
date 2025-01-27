package com.example.customviewwithoutcompose.di.modules

import android.content.Context
import com.example.customviewwithoutcompose.data.local.room.CoinsDao
import com.example.customviewwithoutcompose.data.local.room.CoinsDataBase
import com.example.customviewwithoutcompose.data.local.room.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoinsDataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ): CoinsDataBase {
        return CoinsDataBase.getDataBase(context)
    }

    @Provides
    fun provideCoinsDao(dataBase: CoinsDataBase): CoinsDao {
        return dataBase.coinsDao()
    }

    @Provides
    fun provideNotesDao(database: CoinsDataBase): NotesDao = database.notesDao()

}