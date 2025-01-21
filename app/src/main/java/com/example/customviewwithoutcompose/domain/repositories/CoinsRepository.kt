package com.example.customviewwithoutcompose.domain.repositories

import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.domain.models.CoinDomain
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

//    suspend fun getCoinsShortEntity(): Result<List<CoinDomain>>

    suspend fun getCoinsFullEntity(): Result<List<CoinDomain>>

    suspend fun getCoinById(id: String): Result<CoinDomain>

    suspend fun getTickerById(id: String): Result<CoinDomain>

    suspend fun getCoinsFromDB(): Flow<List<CoinRoomEntity>>

    suspend fun insertCoinsToDB(list: List<CoinDomain>)
}