package com.example.customviewwithoutcompose.data.repositories

import android.util.Log
import com.example.customviewwithoutcompose.core.other.TAG
import com.example.customviewwithoutcompose.data.repositories.CoinsRepositoryImpl.Companion.FILTERING_TYPE
import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

class CoinsRepositoryFake @Inject constructor() : CoinsRepository {

    val fakeCoins = List(1000) { index ->
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
            startedAt = "2010-07-17T00:00:00Z",
            price = 2525.7
        )
    }

    // if delete do it in repo and impl also
//    override suspend fun getCoinsShortEntity(): Result<List<CoinDomain>> {
//        return Result.success(fakeCoins)
//    }

    override suspend fun getCoinsFullEntity(): Result<List<CoinDomain>> {
        return Result.success(fakeCoins)
            .mapCatching { coins ->
                coroutineScope {
                    Log.d(TAG, "getCoins: time start")
                    val list = coins
                        .filter { it.rank > 0 && it.isActive == true && it.type == FILTERING_TYPE }
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
            }
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
}