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
import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import com.example.customviewwithoutcompose.presentation.adapters.CustomListItem
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForAdapter
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinCustomView
import com.example.customviewwithoutcompose.presentation.models.coin.mappers.CoinUiModelMapper.mapToUiModel
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView
import com.example.customviewwithoutcompose.presentation.models.note.mappers.NoteUiModelMapper.mapToNoteUiModel
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
) : ViewModel() {

    val noteList = MutableStateFlow<List<ModelForNoteCustomView>>(emptyList())
    private val coinList = MutableStateFlow<List<ModelForCoinCustomView>>(emptyList())
    private val expandedCoinItemsIds = MutableStateFlow<Set<String>>(emptySet())

    val recyclerItemsList: StateFlow<List<CustomListItem>> =
        combine(noteList, coinList, expandedCoinItemsIds) { notes, coins, expandedIds ->
            val noteItems = notes.map {
                CustomListItem.NoteItem(it)
            }
            val coinItems = coins.map { coin ->
                CustomListItem.CoinItem(
                    ModelForAdapter(
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

    fun onItemToggle(item: ModelForAdapter) {
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
            val note = NoteRoomEntity(
                id = UUID.randomUUID().toString(),
                note = noteText
            )
            notesRepository.addNote(note)
            setAddedPositionToChannel()
        }
    }

    fun deleteNote(note: NoteRoomEntity) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
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
            if (hasInternetConnection()) {
                coinsRepository.fetchCoinsFullEntity()
            } else {
                val message = context.getString(R.string.no_internet_connection)
                _event.send(Events.MessageForUser(message))
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

    private suspend fun setAddedPositionToChannel() {
        delay(50)
        val position = if (noteList.value.isNotEmpty()) {
            noteList.value.size - 1
        } else 0
        _event.send(Events.PositionToScrolling(position))
    }

}