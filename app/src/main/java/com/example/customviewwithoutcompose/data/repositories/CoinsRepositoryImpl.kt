package com.example.customviewwithoutcompose.data.repositories

import android.util.Log
import com.example.customviewwithoutcompose.core.networking.safeApiCall
import com.example.customviewwithoutcompose.core.networking.safeApiCallList
import com.example.customviewwithoutcompose.core.networking.toDomain
import com.example.customviewwithoutcompose.core.networking.toDomainList
import com.example.customviewwithoutcompose.core.other.TAG
import com.example.customviewwithoutcompose.data.network.ApiService
import com.example.customviewwithoutcompose.data.network.entities.mappers.CoinDomainMapper
import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CoinsRepository {

    override suspend fun getCoinsShortEntity(): Result<List<CoinDomain>> {
        return safeApiCallList {
            apiService.getCoins()
        }
            .toDomainList(CoinDomainMapper)
            .mapCatching { coins ->
                Log.d(TAG, "getCoins: time start")
                val list = coins
                    .filter { it.rank > 0 && it.isActive == true && it.type == FILTERING_TYPE }
                    .sortedBy { it.rank }
                    .take(MAX_COUNT_ITEMS)

                Log.d(TAG, "getCoins: time end")
                list

            }
            .onSuccess { coins ->
                Log.d(TAG, "getCoins: success, size = ${coins.size}")
            }
            .onFailure { error ->
                Log.d(TAG, "getCoins(): failure, \nerror = $error")
            }
    }

    override suspend fun getCoinsFullEntity(): Result<List<CoinDomain>> {
        return safeApiCallList {
            apiService.getCoins()
        }
            .toDomainList(CoinDomainMapper)
            .mapCatching { coins ->
                coroutineScope {
                    Log.d(TAG, "getCoins: time start")
                    val list = coins
                        .filter { it.rank > 0 && it.isActive == true && it.type == FILTERING_TYPE }
                        .sortedBy { it.rank }
                        .take(MAX_COUNT_ITEMS)
                        .map { coin ->
                            async(Dispatchers.IO) {
                                getCoinById(coin.id)
                            }
                        }
                        .awaitAll()
                        .map { result ->
                            result.getOrElse { error ->
                                Log.e(TAG, "Error fetching coin by id: $error")
                                null
                            }
                        }
                        .filterNotNull()
                    Log.d(TAG, "getCoins: time end")
                    list
                }
            }
            .onSuccess { coins ->
                Log.d(TAG, "getCoins: success, size = ${coins.size}")
            }
            .onFailure { error ->
                Log.d(TAG, "getCoins(): failure, \nerror = $error")
            }
    }

    override suspend fun getCoinById(id: String): Result<CoinDomain> {
        return safeApiCall {
            apiService.getCoinById(id)
        }.toDomain(CoinDomainMapper)
    }

    companion object {

        private const val MAX_COUNT_ITEMS = 15
        private const val FILTERING_TYPE = "coin"
    }

}