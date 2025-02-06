package com.example.customviewwithoutcompose.domain.usecases

interface DayWithMostNotes {

    suspend fun getDayWithMostNotesCount(): Result<String>

}