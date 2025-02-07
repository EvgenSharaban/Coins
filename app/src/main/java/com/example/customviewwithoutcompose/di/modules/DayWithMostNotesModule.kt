package com.example.customviewwithoutcompose.di.modules

import com.example.customviewwithoutcompose.domain.usecases.DayWithMostNotes
import com.example.customviewwithoutcompose.domain.usecases.DayWithMostNotesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DayWithMostNotesModule {

    @Binds
    abstract fun provideDayWithMostNotes(dayWithMostNotesRepositoryImpl: DayWithMostNotesImpl): DayWithMostNotes

}