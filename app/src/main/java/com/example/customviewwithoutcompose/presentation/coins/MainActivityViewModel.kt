package com.example.customviewwithoutcompose.presentation.coins

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.TAG
import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import com.example.customviewwithoutcompose.presentation.Events
import com.example.customviewwithoutcompose.presentation.base.BaseViewModel
import com.example.customviewwithoutcompose.presentation.coins.adapters.coin.CoinItem
import com.example.customviewwithoutcompose.presentation.coins.adapters.note.NoteItem
import com.example.customviewwithoutcompose.presentation.coins.models.coin.ModelForCoinCustomView
import com.example.customviewwithoutcompose.presentation.coins.models.coin.ModelForCoinsAdapter
import com.example.customviewwithoutcompose.presentation.coins.models.coin.mappers.CoinUiModelMapper.mapToUiModel
import com.example.customviewwithoutcompose.presentation.coins.models.note.ModelForNoteCustomView
import com.example.customviewwithoutcompose.presentation.coins.models.note.mappers.NoteUiModelMapper.mapToNoteUiModel
import com.example.delegateadapter.DelegateAdapterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    private val notesRepository: NotesRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val noteList = MutableStateFlow<List<ModelForNoteCustomView>>(emptyList())
    private val coinList = MutableStateFlow<List<ModelForCoinCustomView>>(emptyList())
    private val expandedCoinItemsIds = MutableStateFlow<Set<String>>(emptySet())

    val recyclerItemsList: StateFlow<List<DelegateAdapterItem>> =
        combine(noteList, coinList, expandedCoinItemsIds) { notes, coins, expandedIds ->
            val noteItems = notes.map {
                NoteItem(it)
            }
            val coinItems = coins.map { coin ->
                CoinItem(
                    ModelForCoinsAdapter(
                        customViewModel = coin,
                        isExpanded = coin.id in expandedIds
                    )
                )
            }
            Log.d(TAG, "notes items size = ${noteItems.size}")
            noteItems.plus(coinItems)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _event = Channel<Events>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        getCoins()
        Log.d(TAG, "getCoins: time 0")
        observeData()
    }

    fun onItemToggle(item: ModelForCoinsAdapter) {
        expandedCoinItemsIds.update {
            if (expandedCoinItemsIds.value.contains(item.customViewModel.id)) {
                it.minus(item.customViewModel.id)
            } else {
                it.plus(item.customViewModel.id)
            }
        }
    }

    fun addNote(noteText: String) {
        viewModelScope.launch {
            setLoading(true)
            val note = NoteRoomEntity(
                id = UUID.randomUUID().toString(),
                note = noteText
            )
            notesRepository.addNote(note)
            setAddedPositionToChannel()
            setLoading(false)
        }
    }

    fun deleteNote(note: NoteRoomEntity) {
        viewModelScope.launch {
            setLoading(true)
            notesRepository.deleteNote(note)
            setLoading(false)
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            coinsRepository.coins.collect { localCoins ->
                val coinsList = localCoins.map { it.mapToUiModel() }
                Log.d(TAG, "observeData: _coinForCustomViewList size = ${coinList.value.size}, coinsList size = ${coinsList.size}")
                if (coinList.value != coinsList) {
                    coinList.update { coinsList }
                }
            }
        }

        viewModelScope.launch {
            notesRepository.notes.collect { localNotes ->
                Log.d(TAG, "observeData: notes size = ${localNotes.size}")
                noteList.update {
                    localNotes.map { note ->
                        note.mapToNoteUiModel()
                    }
                }
            }
        }
    }

    private fun getCoins() {
        viewModelScope.launch {
            setLoading(true)
            if (hasInternetConnection()) {
                coinsRepository.fetchCoinsFullEntity()
                    .onFailure { error ->
                        _event.send(Events.MessageForUser(error.message ?: "Unknown error"))
                    }
            } else {
                val message = context.getString(R.string.no_internet_connection)
                _event.send(Events.MessageForUser(message))
                Log.d(TAG, message)
            }
            setLoading(false)
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private suspend fun setAddedPositionToChannel() {
        delay(50)
        val position = if (noteList.value.isNotEmpty()) {
            noteList.value.size - 1
        } else 0
        _event.send(Events.PositionToScrolling(position))
    }

}