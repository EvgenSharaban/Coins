package com.example.customviewwithoutcompose.data.repositories

import android.util.Log
import androidx.room.withTransaction
import com.example.customviewwithoutcompose.core.other.FAILURE_VALUE
import com.example.customviewwithoutcompose.core.other.TAG
import com.example.customviewwithoutcompose.data.local.datastore.CoinsDataStore
import com.example.customviewwithoutcompose.data.local.room.CoinsDao
import com.example.customviewwithoutcompose.data.local.room.CoinsDataBase
import com.example.customviewwithoutcompose.data.local.room.entities.CoinDataBaseMapper.mapToLocalEntityList
import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.data.repositories.CoinsRepositoryImpl.Companion.FILTERING_TYPE
import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CoinsRepositoryFake @Inject constructor(
    private val dataBase: CoinsDataBase,
    private val coinsDao: CoinsDao,
    private val dataStore: CoinsDataStore
) : CoinsRepository {

    private val allCoins: Flow<List<CoinRoomEntity>> = coinsDao.getAllCoins()
    private val hiddenCoins: Flow<Set<String>> = dataStore.getHiddenCoinsFlow()
    override val coins: Flow<List<CoinRoomEntity>> = combine(allCoins, hiddenCoins) { allCoins, hiddenCoinsIds ->
        allCoins.filter { !hiddenCoinsIds.contains(it.id) }
    }

    private val fakeCoins = List(100) { index ->
        CoinDomain(
            id = (index + 1).toString(),
            name = "Bitcoin Fake",
            symbol = "BTC",
            rank = index + 1,
            isNew = false,
            isActive = true,
            type = "coin",
            logo = "https://www.shutterstock.com/image-vector/crypto-currency-golden-coin-black-600nw-593193626.jpg",
            description = "The first and most popular cryptocurrency. The first and most popular cryptocurrency. The first and most popular cryptocurrency. The first and most popular cryptocurrency.",
            startedAt = if (index == 3) " sjfs--" else "2010-07-17T00:00:00Z",
            price = 2525.7
        )
    }

    override suspend fun fetchCoinsFullEntity(): Result<Unit> {
        return Result.success(fakeCoins)
            .mapCatching { coins ->
                coroutineScope {
                    Log.d(TAG, "getCoins: time start")
                    val list = coins
                        .filter { it.rank > 0 && it.isActive && it.type == FILTERING_TYPE }
                        .sortedBy { it.rank }
                        .map { coin ->
                            async(Dispatchers.IO) {
                                getCoinById(coin.id).getOrNull()
                            }
                        }
                        .awaitAll()
                        .filterNotNull()
                    Log.d(TAG, "getCoins: time end")
                    list
                }
            }.onSuccess {
                insertCoinsToDB(it)
            }
            .map { } // need for Result<Unit>
//        return Result.failure(Exception("manually failed"))
    }

    override suspend fun getCoinById(id: String): Result<CoinDomain> {
        val coin = fakeCoins.find { it.id == id }
        return if (coin != null) {
            delay(3000)
            Result.success(coin)
        } else {
            Result.failure(Exception("Coin with id: $id not found"))
        }
    }

    override suspend fun getTickerById(id: String): Result<CoinDomain> {
        val coin = fakeCoins.find { it.id == id }
        return if (coin != null) {
            Result.success(coin)
        } else {
            Result.failure(Exception("Coin with id: $id not found"))
        }
    }

    override suspend fun getHiddenCoinsCount(): Result<Int> {
        return try {
            delay(1000)
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
}