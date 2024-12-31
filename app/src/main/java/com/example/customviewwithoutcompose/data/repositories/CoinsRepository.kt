package com.example.customviewwithoutcompose.data.repositories

import android.util.Log
import com.example.customviewwithoutcompose.core.networking.safeApiCall
import com.example.customviewwithoutcompose.core.networking.safeApiCallList
import com.example.customviewwithoutcompose.data.network.ApiService
import com.example.customviewwithoutcompose.data.network.entities.CoinEntity
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.utils.TAG
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CoinsRepository {

    override suspend fun getCoins(): Result<List<CoinEntity>> {
        return safeApiCallList {
            apiService.getCoins()
        }
            .onSuccess { coins ->
                Log.d(TAG, "getCoins: success, size = ${coins.size}")
            }
            .onFailure { error ->
                Log.d(TAG, "getCoins(): failure, error = $error")
            }
    }

    override suspend fun getCoinById(id: String): Result<CoinEntity> {
        return safeApiCall {
            apiService.getCoinById(id)
        }
    }

}