package com.example.customviewwithoutcompose.presentation.summary.utility

sealed interface SummaryState {
    class Default : SummaryState
    class Loaded(val value: SummaryUiState) : SummaryState
}