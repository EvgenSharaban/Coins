package com.example.customviewwithoutcompose.di.modules

import com.example.customviewwithoutcompose.data.repositories.NotesRepositoryImpl
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotesModule {

    @Binds
    abstract fun provideNotesRepository(notesRepositoryImpl: NotesRepositoryImpl): NotesRepository

}