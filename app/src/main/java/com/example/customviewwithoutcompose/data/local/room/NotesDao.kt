package com.example.customviewwithoutcompose.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteRoomEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addNote(note: NoteRoomEntity)

    @Delete
    suspend fun deleteNote(note: NoteRoomEntity)

}