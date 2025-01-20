package com.example.customviewwithoutcompose.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.customviewwithoutcompose.data.local.room.entities.CoinLocalEntity

@Dao
interface CoinsDao {

    @Query("SELECT * FROM coins")
    suspend fun getAllCoins(): List<CoinLocalEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAllCoins(coins: List<CoinLocalEntity>)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

}