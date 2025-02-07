package com.example.customviewwithoutcompose.presentation.summary

import androidx.lifecycle.viewModelScope
import com.example.customviewwithoutcompose.domain.repositories.CoinsRepository
import com.example.customviewwithoutcompose.domain.repositories.NotesRepository
import com.example.customviewwithoutcompose.domain.repositories.StatisticRepository
import com.example.customviewwithoutcompose.domain.usecases.DayWithMostNotes
import com.example.customviewwithoutcompose.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryActivityViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository,
    private val notesRepository: NotesRepository,
    private val statisticRepository: StatisticRepository,
    private val dayWithMostNotesUseCase: DayWithMostNotes
) : BaseViewModel() {

    private val _totalItemsCounts = MutableStateFlow<String>("-")
    val totalItemsCounts = _totalItemsCounts.asStateFlow()

    private val _hiddenCoinsCounts = MutableStateFlow<String>("-")
    val hiddenCoinsCounts = _hiddenCoinsCounts.asStateFlow()

    private val _totalNotesCounts = MutableStateFlow<String>("-")
    val totalNotesCounts = _totalNotesCounts.asStateFlow()

    private val _dayWithMostNotes = MutableStateFlow<String>("-")
    val dayWithMostNotes = _dayWithMostNotes.asStateFlow()

    private val _amountOfDaysAppUsing = MutableStateFlow<String>("-")
    val amountOfDaysAppUsing = _amountOfDaysAppUsing.asStateFlow()

    init {
        fetchTotalItemsCount()
        fetchHiddenCoinsCount()
        fetchTotalNotesCount()
        fetchDayWithMostNotes()
        fetchAmountOfDaysAppUsing()
    }

    private fun fetchTotalItemsCount() {
        viewModelScope.launch {
            setLoading(true)
            statisticRepository.getTotalItemsCount()
                .onSuccess {
                    _totalItemsCounts.value = it.toString()
                }
                .onFailure {
                    _totalItemsCounts.value = "-"
                }
            setLoading(false)
        }
    }

    private fun fetchHiddenCoinsCount() {
        viewModelScope.launch {
            coinsRepository.getHiddenCoinsCount()
                .onSuccess {
                    _hiddenCoinsCounts.value = it.toString()
                }
                .onFailure {
                    _hiddenCoinsCounts.value = "-"
                }
        }
    }

    private fun fetchDayWithMostNotes() {
        viewModelScope.launch {
            dayWithMostNotesUseCase.getDayWithMostNotesCount()
                .onSuccess {
                    _dayWithMostNotes.value = it
                }
                .onFailure {
                    _dayWithMostNotes.value = "-"
                }
        }
    }

    private fun fetchTotalNotesCount() {
        viewModelScope.launch {
            notesRepository.getTotalNotesCount()
                .onSuccess {
                    _totalNotesCounts.value = it.toString()
                }
                .onFailure {
                    _totalNotesCounts.value = "-"
                }
        }
    }

    private fun fetchAmountOfDaysAppUsing() {
        viewModelScope.launch {
            statisticRepository.getAmountOfDaysAppUsing()
                .onSuccess {
                    _amountOfDaysAppUsing.value = it.toString()
                }
                .onFailure {
                    _amountOfDaysAppUsing.value = "-"
                }
        }
    }

}