package com.example.customviewwithoutcompose.presentation.models.note.mappers

import com.example.customviewwithoutcompose.data.local.room.entities.NoteRoomEntity
import com.example.customviewwithoutcompose.presentation.models.note.ModelForNoteCustomView

object NoteUiModelMapper {

    fun NoteRoomEntity.mapToNoteUiModel(): ModelForNoteCustomView {
        return ModelForNoteCustomView(
            id = this.id,
            note = this.note
        )
    }

    fun ModelForNoteCustomView.mapToRoomModel(): NoteRoomEntity {
        return NoteRoomEntity(
            id = this.id,
            note = this.note
        )
    }
}