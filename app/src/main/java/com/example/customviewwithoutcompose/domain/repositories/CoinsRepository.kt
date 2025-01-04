package com.example.customviewwithoutcompose.domain.repositories

import com.example.customviewwithoutcompose.domain.models.ModelForCustomView

interface CoinsRepository {

    suspend fun getCoins(): Result<List<ModelForCustomView>>

    suspend fun getCoinById(id: String): Result<ModelForCustomView>
}