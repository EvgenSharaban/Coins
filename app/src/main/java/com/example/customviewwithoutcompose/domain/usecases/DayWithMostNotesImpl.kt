package com.example.customviewwithoutcompose.domain.usecases

import kotlinx.coroutines.delay
import javax.inject.Inject

class DayWithMostNotesImpl @Inject constructor() : DayWithMostNotes {

    override suspend fun getDayWithMostNotesCount(): Result<String> {
        delay(1500)
        return Result.success("12.25.2025")
//        return Result.failure(Exception(FAILURE_VALUE))
    }

}