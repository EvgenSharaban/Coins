package com.example.customviewwithoutcompose.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinLocalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val isNew: Boolean,
    val isActive: Boolean,
    val type: String,
    val logo: String,
    val description: String,
    val startedAt: String,
    var price: Double
)
