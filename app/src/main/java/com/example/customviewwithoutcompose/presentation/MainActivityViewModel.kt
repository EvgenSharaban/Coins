package com.example.customviewwithoutcompose.presentation

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.data.network.entities.CoinEntity
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val coinModel = MutableLiveData<CoinEntity>()

    private val _event = Channel<String>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        getCoins()
    }

    private fun getCoins() {
        if (hasInternetConnection()) {
            viewModelScope.launch {
                coinsRepository.getCoins().onSuccess { coins ->
                    val list = coins
                        .filter { it.rank != null && it.rank > 0 && it.isActive == true && it.type == "coin" }
                        .sortedBy { it.rank }
                    list.take(1).forEach { coin ->
                        if (coin.id != null) {
                            coinsRepository.getCoinById(coin.id).onSuccess { item ->
                                coinModel.value = item
                                Log.d(TAG, "${coin.name}: $item")
                            }
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                val message = context.getString(R.string.no_internet_connection)
                _event.trySend(message)
                Log.d(TAG, message)
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//        return when {
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            else -> false
//        }
    }
}