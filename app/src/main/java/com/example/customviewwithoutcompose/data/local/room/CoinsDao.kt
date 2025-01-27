package com.example.customviewwithoutcompose.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinsDao {

    @Query("SELECT * FROM coins")
    fun getAllCoins(): Flow<List<CoinRoomEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertAllCoins(coins: List<CoinRoomEntity>)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

}