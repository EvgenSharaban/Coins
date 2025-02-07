package com.example.customviewwithoutcompose.data.repositories

import com.example.customviewwithoutcompose.data.local.room.NotesDao
import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
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
//        return Result.success(8)
        return Result.failure(Exception("-"))
    }

}