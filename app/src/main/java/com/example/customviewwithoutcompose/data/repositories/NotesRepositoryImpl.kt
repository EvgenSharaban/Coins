package com.example.customviewwithoutcompose.data.repositories

import com.example.customviewwithoutcompose.core.other.FAILURE_VALUE
import com.example.customviewwithoutcompose.data.local.room.NotesDao
import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao
) : NotesRepository {

    override val notes: Flow<List<NoteRoomEntity>> = notesDao.getAllNotes()

    override suspend fun addNote(note: NoteRoomEntity): Result<Unit> {
        try {
            notesDao.addNote(note)
            return Result.success(Unit)
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteNote(note: NoteRoomEntity): Result<Unit> {
        return try {
            notesDao.deleteNote(note)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun getTotalNotesCount(): Result<Int> {
        delay(1500)
        return try {
            val list = notesDao.getAllNotes().first()
            Result.success(list.size)
        } catch (e: Throwable) {
            Result.failure(Exception(FAILURE_VALUE, e))
        }
    }

}