package com.example.customviewwithoutcompose.presentation

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.TAG
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.presentation.uimodels.ModelForCustomView
import com.example.customviewwithoutcompose.presentation.uimodels.mappers.CoinUiModelMapper.mapToUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val coinsList = MutableStateFlow<List<ModelForCustomView>>(emptyList())

    private val _event = Channel<String>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        getCoins()
        Log.d(TAG, "getCoins: time 0")
    }

    private fun getCoins() {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                coinsRepository.getCoinsFullEntity().onSuccess { coins ->
                    coinsList.update { coins.map { it.mapToUiModel() } }
                }
            } else {
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