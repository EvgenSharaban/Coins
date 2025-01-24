package com.example.customviewwithoutcompose.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinsDao {

    @Query("SELECT * FROM coins")
    fun getAllCoins(): Flow<List<CoinRoomEntity>>

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteRoomEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertAllCoins(coins: List<CoinRoomEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun addNote(note: NoteRoomEntity)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

    @Delete
    suspend fun deleteNote(note: NoteRoomEntity)

}