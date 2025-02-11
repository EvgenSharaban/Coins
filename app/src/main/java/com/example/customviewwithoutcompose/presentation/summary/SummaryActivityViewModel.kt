package com.example.customviewwithoutcompose.presentation.summary

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.core.other.FAILURE_VALUE
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import com.example.customviewwithoutcompose.domain.repositories.StatisticRepository
import com.example.customviewwithoutcompose.domain.usecases.DayWithMostNotes
import com.example.customviewwithoutcompose.presentation.Events
import com.example.customviewwithoutcompose.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SummaryActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    private val notesRepository: NotesRepository,
    private val statisticRepository: StatisticRepository,
    private val dayWithMostNotesUseCase: DayWithMostNotes,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _totalItemsCounts = MutableStateFlow<SummaryState>(SummaryState.Default())
    val totalItemsCounts = _totalItemsCounts.asStateFlow()

    private val _hiddenCoinsCounts = MutableStateFlow<SummaryState>(SummaryState.Default())
    val hiddenCoinsCounts = _hiddenCoinsCounts.asStateFlow()

    private val _totalNotesCounts = MutableStateFlow<SummaryState>(SummaryState.Default())
    val totalNotesCounts = _totalNotesCounts.asStateFlow()

    private val _dayWithMostNotes = MutableStateFlow<SummaryState>(SummaryState.Default())
    val dayWithMostNotes = _dayWithMostNotes.asStateFlow()

    private val _amountOfDaysAppUsing = MutableStateFlow<SummaryState>(SummaryState.Default())
    val amountOfDaysAppUsing = _amountOfDaysAppUsing.asStateFlow()

    private val _event = Channel<Events>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            setLoading(true)
            fetchAllData()
            setLoading(false)
        }
    }

    private suspend fun fetchAllData() = withContext(Dispatchers.IO) {
        val totalItems = async { statisticRepository.getTotalItemsCount() }
        val hiddenCoins = async { coinsRepository.getHiddenCoinsCount() }
        val totalNotes = async { notesRepository.getTotalNotesCount() }
        val dayWithMostNotes = async { dayWithMostNotesUseCase.getDayWithMostNotesCount() }
        val amountOfDaysAppUsing = async { statisticRepository.getAmountOfDaysAppUsing() }

        val totalItemsResult = totalItems.await()
        val hiddenCoinsResult = hiddenCoins.await()
        val totalNotesResult = totalNotes.await()
        val dayWithMostNotesResult = dayWithMostNotes.await()
        val amountOfDaysAppUsingResult = amountOfDaysAppUsing.await()

        listOf(
            totalItemsResult,
            hiddenCoinsResult,
            totalNotesResult,
            dayWithMostNotesResult,
            amountOfDaysAppUsingResult
        ).forEach {
            val exception = it.exceptionOrNull()
            if (exception != null) {
                _event.send(Events.MessageForUser(exception.message ?: context.getString(R.string.unknown_error)))
                return@forEach
            }
        }

        _totalItemsCounts.update {
            val string = totalItemsResult.getOrNull() ?: FAILURE_VALUE
            SummaryState.Loaded(string.toString())
        }

        _hiddenCoinsCounts.update {
            val string = hiddenCoinsResult.getOrNull() ?: FAILURE_VALUE
            SummaryState.Loaded(string.toString())
        }

        _totalNotesCounts.update {
            val string = totalNotesResult.getOrNull() ?: FAILURE_VALUE
            SummaryState.Loaded(string.toString())
        }

        _dayWithMostNotes.update {
            val string = dayWithMostNotesResult.getOrNull() ?: FAILURE_VALUE
            SummaryState.Loaded(string.toString())
        }

        _amountOfDaysAppUsing.update {
            val string = amountOfDaysAppUsingResult.getOrNull() ?: FAILURE_VALUE
            SummaryState.Loaded(string.toString())
        }
    }

    sealed interface SummaryState {
        class Default : SummaryState
        class Loaded(val value: String) : SummaryState
    }

}