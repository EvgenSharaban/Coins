package com.example.customviewwithoutcompose.data.repositories

import android.util.Log
import androidx.room.withTransaction
import com.example.customviewwithoutcompose.core.networking.safeApiCall
import com.example.customviewwithoutcompose.core.networking.safeApiCallList
import com.example.customviewwithoutcompose.core.networking.toDomain
import com.example.customviewwithoutcompose.core.networking.toDomainList
import com.example.customviewwithoutcompose.core.other.FAILURE_VALUE
import com.example.customviewwithoutcompose.core.other.TAG
import com.example.customviewwithoutcompose.core.other.roundTo
import com.example.customviewwithoutcompose.data.local.datastore.CoinsDataStore
import com.example.customviewwithoutcompose.data.local.room.CoinsDao
import com.example.customviewwithoutcompose.data.local.room.CoinsDataBase
import com.example.customviewwithoutcompose.data.local.room.entities.CoinDataBaseMapper.mapToLocalEntityList
import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.data.network.ApiService
import com.example.customviewwithoutcompose.data.network.entities.mappers.CoinDomainMapper
import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataBase: CoinsDataBase,
    private val coinsDao: CoinsDao,
    private val dataStore: CoinsDataStore
) : CoinsRepository {

    private val allCoins: Flow<List<CoinRoomEntity>> = coinsDao.getAllCoins()
    private val hiddenCoins: Flow<Set<String>> = dataStore.getHiddenCoinsFlow()
    override val coins: Flow<List<CoinRoomEntity>> = combine(allCoins, hiddenCoins) { allCoins, hiddenCoinsIds ->
        allCoins.filter { !hiddenCoinsIds.contains(it.id) }
    }

    override suspend fun fetchCoinsFullEntity(): Result<Unit> {
        return safeApiCallList {
            apiService.getCoins()
        }
            .toDomainList(CoinDomainMapper)
            .mapCatching { coins ->
                getDetailInfoByList(coins)
            }
            .onSuccess { coins ->
                insertCoinsToDB(coins)
                Log.d(TAG, "getCoins: success, size = ${coins.size}")
            }
            .onFailure { error ->
                Log.d(TAG, "getCoins(): failure, \nerror = $error")
            }
            .map { } // need for Result<Unit>
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

    override suspend fun getHiddenCoinsCount(): Result<Int> {
        return try {
            val list = dataStore.getHidedCoinsIds()
            Result.success(list.size)
        } catch (e: Throwable) {
            Result.failure(Exception(FAILURE_VALUE, e))
        }
    }

    override suspend fun hideCoin(id: String) {
        dataStore.addHidedCoinId(id)
        Log.d(TAG, "onCleared: hidden item = $id")
    }

    override suspend fun getHidedCoinsIds(): Set<String> {
        return dataStore.getHidedCoinsIds()
    }

    private suspend fun getDetailInfoByList(coins: List<CoinDomain>): List<CoinDomain> = coroutineScope {
        Log.d(TAG, "getCoins: time start")
        val list = coins
            .filter { it.rank > 0 && it.isActive && it.type == FILTERING_TYPE }
            .sortedBy { it.rank }
            .take(MAX_COUNT_ITEMS)
            .map { coin ->
                async {
                    getDetailInfo(coin)
                }
            }
            .awaitAll()
            .mapNotNull { result ->
                result.getOrElse { error ->
                    Log.e(TAG, "Error fetching coin by id: $error")
                    null
                }
            }
        Log.d(TAG, "getCoins: time end")
        list
    }

    private suspend fun getDetailInfo(coin: CoinDomain): Result<CoinDomain> = getCoinById(coin.id)
        .mapCatching { fetchedCoin ->
            val price = getTickerById(fetchedCoin.id).getOrThrow().price
            val roundedPrice = price.roundTo(NUMBERS_OF_DIGITS_PRICE_AFTER_POINT)
            fetchedCoin.copy(price = roundedPrice)
        }

    private suspend fun insertCoinsToDB(list: List<CoinDomain>) {
        try {
            dataBase.withTransaction {
                coinsDao.deleteAllCoins()
                coinsDao.insertAllCoins(list.mapToLocalEntityList())
            }
            Log.d(TAG, "insertCoinsToDB: success")
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