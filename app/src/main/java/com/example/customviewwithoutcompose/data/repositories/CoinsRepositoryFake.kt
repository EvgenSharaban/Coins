package com.example.customviewwithoutcompose.data.repositories

import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import javax.inject.Inject

class CoinsRepositoryFake @Inject constructor() : CoinsRepository {

    private val fakeCoins = listOf(
        CoinDomain(
            id = "1",
            name = "BitcoinFake",
            symbol = "BTC",
            rank = 1,
            isNew = false,
            isActive = true,
            type = "coin",
            logo = "https://www.shutterstock.com/image-vector/crypto-currency-golden-coin-black-600nw-593193626.jpg",
            description = "The first and most popular cryptocurrency.",
            startedAt = "2009-01-03"
        ),
        CoinDomain(
            id = "2",
            name = "EthereumFake",
            symbol = "ETH",
            rank = 2,
            isNew = false,
            isActive = true,
            type = "coin",
            logo = "https://w7.pngwing.com/pngs/751/919/png-transparent-ethereum-coin-icon-luxury-golden-design.png",
            description = "A decentralized platform for smart contracts.",
            startedAt = "2015-07-30"
        ),
        CoinDomain(
            id = "3",
            name = "LiteCoinFake",
            symbol = "LTC",
            rank = 3,
            isNew = false,
            isActive = true,
            type = "coin",
            logo = "https://static.vecteezy.com/system/resources/previews/011/307/286/non_2x/litecoin-ltc-badge-crypto-3d-rendering-free-png.png",
            description = "A peer-to-peer cryptocurrency.",
            startedAt = "2011-10-07"
        )
    )

    override suspend fun getCoins(): Result<List<CoinDomain>> {
        return Result.success(fakeCoins)
    }

    override suspend fun getCoinById(id: String): Result<CoinDomain> {
        val coin = fakeCoins.find { it.id == id }
        return if (coin != null) {
            Result.success(coin)
        } else {
            Result.failure(Exception("Coin with id: $id not found"))
        }
    }
}