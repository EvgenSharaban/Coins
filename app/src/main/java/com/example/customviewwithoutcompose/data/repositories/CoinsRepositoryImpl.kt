package com.example.customviewwithoutcompose.data.repositories

import android.util.Log
import com.example.customviewwithoutcompose.core.networking.safeApiCall
import com.example.customviewwithoutcompose.core.networking.safeApiCallList
import com.example.customviewwithoutcompose.core.networking.toDomain
import com.example.customviewwithoutcompose.core.networking.toDomainList
import com.example.customviewwithoutcompose.core.other.TAG
import com.example.customviewwithoutcompose.core.other.roundTo
import com.example.customviewwithoutcompose.data.local.room.CoinsDataBase
import com.example.customviewwithoutcompose.data.local.room.entities.CoinDataBaseMapper.mapToLocalEntityList
import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.data.network.ApiService
import com.example.customviewwithoutcompose.data.network.entities.mappers.CoinDomainMapper
import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataBase: CoinsDataBase
) : CoinsRepository {

//    override suspend fun getCoinsShortEntity(): Result<List<CoinDomain>> {
//        return safeApiCallList {
//            apiService.getCoins()
//        }
//            .toDomainList(CoinDomainMapper)
//            .mapCatching { coins ->
//                Log.d(TAG, "getCoins: time start")
//                val list = coins
//                    .filter { it.rank > 0 && it.isActive == true && it.type == FILTERING_TYPE }
//                    .sortedBy { it.rank }
//                    .take(MAX_COUNT_ITEMS)
//
//                Log.d(TAG, "getCoins: time end")
//                list
//
//            }
//            .onSuccess { coins ->
//                Log.d(TAG, "getCoins: success, size = ${coins.size}")
//            }
//            .onFailure { error ->
//                Log.d(TAG, "getCoins(): failure, \nerror = $error")
//            }
//    }

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
                            async {
                                getCoinById(coin.id)
                                    .mapCatching { fetchedCoin ->
                                        async {
                                            val price = getTickerById(fetchedCoin.id).getOrThrow().price
                                            val roundedPrice = price.roundTo(NUMBERS_OF_DIGITS_PRICE_AFTER_POINT)
                                            fetchedCoin.copy(price = roundedPrice)
                                        }.await()
                                    }
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
                insertCoinsToDB(coins)
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

    override suspend fun getTickerById(id: String): Result<CoinDomain> {
        return safeApiCall {
            apiService.getTickerById(id)
        }.toDomain(CoinDomainMapper)
    }

    override suspend fun getCoinsFromDB(): Flow<List<CoinRoomEntity>> {
        return try {
            val list = dataBase.coinsDao().getAllCoins()
            Log.d(TAG, "getCoinsFromDB: success")
            list
        } catch (e: Throwable) {
            Log.d(TAG, "getCoinsFromDB: failed, \nerror = $e")
            flow { emptyList<CoinRoomEntity>() }
        }
    }

    override suspend fun insertCoinsToDB(list: List<CoinDomain>) {
        try {
            withContext(Dispatchers.IO) {
                dataBase.coinsDao().deleteAllCoins()
                dataBase.coinsDao().insertAllCoins(list.mapToLocalEntityList())
                Log.d(TAG, "insertCoinsToDB: success")
            }
        } catch (e: Throwable) {
            Log.d(TAG, "insertCoinsToDB: failed, \nerror = $e")
        }
    }

    companion object {

        private const val NUMBERS_OF_DIGITS_PRICE_AFTER_POINT = 2
        private const val MAX_COUNT_ITEMS = 10
        const val FILTERING_TYPE = "coin"
    }

}