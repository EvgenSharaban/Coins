package com.example.customviewwithoutcompose.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteRoomEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "note") val note: String
)
