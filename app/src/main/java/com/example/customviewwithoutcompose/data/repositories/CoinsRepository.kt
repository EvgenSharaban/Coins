package com.example.customviewwithoutcompose.data.repositories

import android.util.Log
import com.example.customviewwithoutcompose.core.networking.safeApiCall
import com.example.customviewwithoutcompose.core.networking.safeApiCallList
import com.example.customviewwithoutcompose.core.networking.toDomain
import com.example.customviewwithoutcompose.core.networking.toDomainList
import com.example.customviewwithoutcompose.data.network.ApiService
import com.example.customviewwithoutcompose.data.network.entities.mappers.CoinMapper
import com.example.customviewwithoutcompose.domain.models.ModelForCustomView
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.utils.TAG
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CoinsRepository {

    override suspend fun getCoins(): Result<List<ModelForCustomView>> {
        return safeApiCallList {
            apiService.getCoins()
        }
            .toDomainList(CoinMapper)
            .mapCatching { coins ->
                coins
                    .filter { it.rank > 0 && it.isActive == true && it.type == "coin" }
                    .sortedBy { it.rank }
                    .take(10)
            }
            .onSuccess { coins ->
                Log.d(TAG, "getCoins: success, size = ${coins.size}")
            }
            .onFailure { error ->
                Log.d(TAG, "getCoins(): failure, \nerror = $error")
            }
    }

    override suspend fun getCoinById(id: String): Result<ModelForCustomView> {
        return safeApiCall {
            apiService.getCoinById(id)
        }.toDomain(CoinMapper)
    }

}