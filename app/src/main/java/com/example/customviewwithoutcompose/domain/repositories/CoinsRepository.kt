package com.example.customviewwithoutcompose.domain.repositories

import com.example.customviewwithoutcompose.domain.models.CoinDomain

interface CoinsRepository {

    suspend fun getCoins(): Result<List<CoinDomain>>

    suspend fun getCoinById(id: String): Result<CoinDomain>
}