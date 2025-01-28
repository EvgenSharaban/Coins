package com.example.customviewwithoutcompose.presentation.adapters

import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinsAdapter
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView

sealed interface CustomListItem {

    data class NoteItem(val note: ModelForNoteCustomView) : CustomListItem
    data class CoinItem(val coin: ModelForCoinsAdapter) : CustomListItem

}