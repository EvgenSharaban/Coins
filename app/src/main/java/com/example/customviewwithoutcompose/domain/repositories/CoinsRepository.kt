package com.example.customviewwithoutcompose.domain.repositories

import com.example.customviewwithoutcompose.data.network.entities.CoinEntity

interface CoinsRepository {

    suspend fun getCoins(): Result<List<CoinEntity>>

    suspend fun getCoinById(id: String): Result<CoinEntity>
}