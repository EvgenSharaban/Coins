package com.example.customviewwithoutcompose.presentation.adapters.note

import com.example.customviewwithoutcompose.presentation.adapters.ContentComparable
import com.example.customviewwithoutcompose.presentation.adapters.DelegateAdapterItem
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView

data class NoteItem(
    val note: ModelForNoteCustomView
) : DelegateAdapterItem {

    override fun id(): String {
        return note.id
    }

    override fun content(): ContentComparable {
        return NoteContent(note)
    }


    inner class NoteContent(val note: ModelForNoteCustomView) : ContentComparable {
        override fun equals(other: Any?): Boolean {
            if (other is NoteContent) {
                return note == other.note
            }
            return false
        }

        override fun hashCode(): Int {
            return 125 * note.hashCode()
        }
    }
}
