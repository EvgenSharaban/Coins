package com.example.customviewwithoutcompose.presentation.adapters

import com.example.customviewwithoutcompose.presentation.models.coin.ModelForAdapter
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView

sealed class CustomListItem {

    data class NoteItem(val note: ModelForNoteCustomView) : CustomListItem()
    data class CoinItem(val coin: ModelForAdapter) : CustomListItem()

}