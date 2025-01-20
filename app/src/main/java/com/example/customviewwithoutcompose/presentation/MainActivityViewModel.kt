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
import com.example.customviewwithoutcompose.presentation.models.ModelForAdapter
import com.example.customviewwithoutcompose.presentation.models.ModelForCustomView
import com.example.customviewwithoutcompose.presentation.models.mappers.CoinUiModelMapper.mapToUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _coinForCustomViewList = MutableStateFlow<List<ModelForCustomView>>(emptyList())
    private val _coinForAdapterList = MutableStateFlow<List<ModelForAdapter>>(emptyList())
    val coinsList = _coinForAdapterList.asStateFlow()

    private val _event = Channel<String>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        getCoins()
        Log.d(TAG, "getCoins: time 0")

        viewModelScope.launch {
            _coinForCustomViewList.map { coins ->
                coins.map { coin ->
                    ModelForAdapter(
                        customViewModel = coin,
                        isExpanded = false
                    )
                }
            }
                .collectLatest { list ->
                    _coinForAdapterList.update { list }
                }
        }
    }

    private fun getCoins() {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                coinsRepository.getCoinsFullEntity().onSuccess { coins ->
                    _coinForCustomViewList.update { coins.map { it.mapToUiModel() } }
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
    }

    fun onItemToggle(item: ModelForAdapter) {
        _coinForAdapterList.update { coins ->
            coins.map { coin ->
                if (coin.customViewModel.id == item.customViewModel.id) {
                    coin.copy(isExpanded = !coin.isExpanded)
                } else {
                    coin
                }
            }
        }
    }
}