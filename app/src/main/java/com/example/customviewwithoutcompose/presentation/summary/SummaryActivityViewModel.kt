package com.example.customviewwithoutcompose.presentation.summary

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import com.example.customviewwithoutcompose.core.other.FAILURE_VALUE
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import com.example.customviewwithoutcompose.domain.repositories.StatisticRepository
import com.example.customviewwithoutcompose.domain.usecases.DayWithMostNotes
import com.example.customviewwithoutcompose.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    init {
        fetchTotalItemsCount()
        fetchHiddenCoinsCount()
        fetchTotalNotesCount()
        fetchDayWithMostNotes()
        fetchAmountOfDaysAppUsing()
    }

    fun setView(
        view: AppCompatTextView,
        state: SummaryState,
        @StringRes resource: Int
    ) {
        when (state) {
            is SummaryState.Default -> {
                view.isVisible = false
            }

            is SummaryState.Loaded -> {
                val string = if (state.value == FAILURE_VALUE) {
                    context.getString(resource, state.value).substringBefore(state.value) + state.value
                } else {
                    context.getString(resource, state.value)
                }
                view.text = string
                view.isVisible = true
            }
        }
    }

    private fun fetchTotalItemsCount() {
        viewModelScope.launch {
            setLoading(true)
            statisticRepository.getTotalItemsCount()
                .onSuccess {
                    _totalItemsCounts.value = SummaryState.Loaded(it.toString())
                }
                .onFailure { error ->
                    _totalItemsCounts.value = SummaryState.Loaded(error.message ?: FAILURE_VALUE)
                }
            setLoading(false)
        }
    }

    private fun fetchHiddenCoinsCount() {
        viewModelScope.launch {
            coinsRepository.getHiddenCoinsCount()
                .onSuccess {
                    _hiddenCoinsCounts.value = SummaryState.Loaded(it.toString())
                }
                .onFailure { error ->
                    _hiddenCoinsCounts.value = SummaryState.Loaded(error.message ?: FAILURE_VALUE)
                }
        }
    }

    private fun fetchDayWithMostNotes() {
        viewModelScope.launch {
            dayWithMostNotesUseCase.getDayWithMostNotesCount()
                .onSuccess {
                    _dayWithMostNotes.value = SummaryState.Loaded(it)
                }
                .onFailure { error ->
                    _dayWithMostNotes.value = SummaryState.Loaded(error.message ?: FAILURE_VALUE)
                }
        }
    }

    private fun fetchTotalNotesCount() {
        viewModelScope.launch {
            notesRepository.getTotalNotesCount()
                .onSuccess {
                    _totalNotesCounts.value = SummaryState.Loaded(it.toString())
                }
                .onFailure { error ->
                    _totalNotesCounts.value = SummaryState.Loaded(error.message ?: FAILURE_VALUE)
                }
        }
    }

    private fun fetchAmountOfDaysAppUsing() {
        viewModelScope.launch {
            statisticRepository.getAmountOfDaysAppUsing()
                .onSuccess {
                    _amountOfDaysAppUsing.value = SummaryState.Loaded(it.toString())
                }
                .onFailure { error ->
                    _amountOfDaysAppUsing.value = SummaryState.Loaded(error.message ?: FAILURE_VALUE)
                }
        }
    }

    sealed interface SummaryState {
        class Default : SummaryState
        class Loaded(val value: String) : SummaryState
    }

}