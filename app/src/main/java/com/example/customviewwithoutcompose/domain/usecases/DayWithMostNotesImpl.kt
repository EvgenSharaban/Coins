package com.example.customviewwithoutcompose.domain.usecases

class DayWithMostNotesImpl : DayWithMostNotes {
    override suspend fun getDayWithMostNotesCount(): Result<String> {
        return Result.success("12.25.2025")
    }
}