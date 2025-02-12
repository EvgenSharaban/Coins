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
import com.example.customviewwithoutcompose.presentation.summary.utility.SummaryState
import com.example.customviewwithoutcompose.presentation.summary.utility.SummaryUiState
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

    private val _summaryUiState = MutableStateFlow<SummaryState>(SummaryState.Default())
    val summaryUiState = _summaryUiState.asStateFlow()

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

        _summaryUiState.update {
            SummaryState.Loaded(
                SummaryUiState(
                    totalItemsCounts = totalItemsResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                    hiddenCoinsCounts = hiddenCoinsResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                    totalNotesCounts = totalNotesResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                    dayWithMostNotes = dayWithMostNotesResult.getOrNull()?.toString() ?: FAILURE_VALUE,
                    amountOfDaysAppUsing = amountOfDaysAppUsingResult.getOrNull()?.toString() ?: FAILURE_VALUE
                )
            )
        }
    }

}