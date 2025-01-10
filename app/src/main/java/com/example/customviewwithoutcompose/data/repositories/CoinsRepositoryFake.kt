package com.example.customviewwithoutcompose.data.repositories

import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

class CoinsRepositoryFake @Inject constructor() : CoinsRepository {

    val fakeCoins = List(10000) { index ->
        CoinDomain(
            id = (index + 1).toString(),
            name = "BitcoinFake",
            symbol = "BTC",
            rank = index + 1,
            isNew = false,
            isActive = true,
            type = "coin",
            logo = "https://www.shutterstock.com/image-vector/crypto-currency-golden-coin-black-600nw-593193626.jpg",
            description = "The first and most popular cryptocurrency.",
            startedAt = "2009-01-03"
        )
    }

    override suspend fun getCoinsShortEntity(): Result<List<CoinDomain>> {
        return Result.success(fakeCoins)
    }

    override suspend fun getCoinsFullEntity(): Result<List<CoinDomain>> {
        return Result.success(fakeCoins)
            .mapCatching { coins ->
                coroutineScope {
                    coins
                        .map { coin ->
                            async(Dispatchers.IO) {
                                getCoinById(coin.id).getOrNull()
                            }
                        }
                        .awaitAll()
                        .filterNotNull()
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
}