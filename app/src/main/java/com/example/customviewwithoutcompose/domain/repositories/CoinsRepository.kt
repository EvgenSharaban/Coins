package com.example.customviewwithoutcompose.domain.repositories

import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.domain.models.CoinDomain
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    val coins: Flow<List<CoinRoomEntity>>

//    suspend fun getCoinsShortEntity(): Result<List<CoinDomain>>

    suspend fun fetchCoinsFullEntity(): Result<Unit>

    suspend fun getCoinById(id: String): Result<CoinDomain>

    suspend fun getTickerById(id: String): Result<CoinDomain>

    suspend fun getTotalCoinsCount(): Result<Int>

    suspend fun getHiddenCoinsCount(): Result<Int>

}