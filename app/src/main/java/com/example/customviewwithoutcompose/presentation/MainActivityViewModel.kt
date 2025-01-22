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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val coinForCustomViewList = MutableStateFlow<List<ModelForCustomView>>(emptyList())

    private val expandedIds = MutableStateFlow<Set<String>>(emptySet())

    val coinsList: StateFlow<List<ModelForAdapter>> =
        combine(coinForCustomViewList, expandedIds) { coins, expandedIds ->
            coins.map { coin ->
                ModelForAdapter(
                    customViewModel = coin,
                    isExpanded = coin.id in expandedIds
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _event = Channel<String>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        getCoins()
        Log.d(TAG, "getCoins: time 0")
        observeData()
    }

    fun onItemToggle(item: ModelForAdapter) {
        expandedIds.update {
            if (expandedIds.value.contains(item.customViewModel.id)) {
                it.minus(item.customViewModel.id)
            } else {
                it.plus(item.customViewModel.id)
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch(Dispatchers.IO) {
            coinsRepository.coins.collect { localCoins ->
                val coinsList = localCoins.map { it.mapToUiModel() }
                Log.d(TAG, "observeData: _coinForCustomViewList size = ${coinForCustomViewList.value.size}, coinsList size = ${coinsList.size}")
                if (coinForCustomViewList.value != coinsList) {
                    coinForCustomViewList.update { coinsList }
                }
            }
        }
    }

    private fun getCoins() {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                coinsRepository.fetchCoinsFullEntity()
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

}