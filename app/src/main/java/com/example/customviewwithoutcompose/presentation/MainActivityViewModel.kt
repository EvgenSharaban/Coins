package com.example.customviewwithoutcompose.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customviewwithoutcompose.data.network.entities.CoinEntity
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
) : ViewModel() {

    val coinLD = MutableLiveData<CoinEntity>()

    init {
        getCoins()
    }

    private fun getCoins() {
        viewModelScope.launch {
            coinsRepository.getCoins().onSuccess { coins ->
                val list = coins
                    .filter { it.rank != null && it.rank > 0 && it.isActive == true && it.type == "coin" }
                    .sortedBy { it.rank }
                list.take(1).forEach { coin ->
                    if (coin.id != null) {
                        coinsRepository.getCoinById(coin.id).onSuccess { item ->
                            coinLD.value = item
                            Log.d(TAG, "${coin.name}: $item")
                        }
                    }
                }
            }
        }
    }
}