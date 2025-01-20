package com.example.customviewwithoutcompose.domain.repositories

import com.example.customviewwithoutcompose.domain.models.CoinDomain

interface CoinsRepository {

//    suspend fun getCoinsShortEntity(): Result<List<CoinDomain>>

    suspend fun getCoinsFullEntity(): Result<List<CoinDomain>>

    suspend fun getCoinById(id: String): Result<CoinDomain>

    suspend fun getTickerById(id: String): Result<CoinDomain>

    suspend fun getCoinsFromRoom(): List<CoinDomain>

    suspend fun insertCoinsToRoom(list: List<CoinDomain>)
}