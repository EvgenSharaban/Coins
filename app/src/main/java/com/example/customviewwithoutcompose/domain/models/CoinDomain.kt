package com.example.customviewwithoutcompose.domain.models

data class CoinDomain(
    val id: String,
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