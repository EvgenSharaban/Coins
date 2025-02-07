package com.example.customviewwithoutcompose.domain.usecases

import javax.inject.Inject

class DayWithMostNotesImpl @Inject constructor() : DayWithMostNotes {

    override suspend fun getDayWithMostNotesCount(): Result<String> {
//        return Result.success("12.25.2025")
        return Result.failure(Exception("-"))
    }

}