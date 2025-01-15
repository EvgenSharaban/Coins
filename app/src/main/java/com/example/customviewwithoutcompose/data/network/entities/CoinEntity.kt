package com.example.customviewwithoutcompose.data.network.entities

import com.google.gson.annotations.SerializedName

data class CoinEntity(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("symbol")
    val symbol: String? = null,

    @field:SerializedName("rank")
    val rank: Int? = null,

    @field:SerializedName("is_new")
    val isNew: Boolean? = null,

    @field:SerializedName("is_active")
    val isActive: Boolean? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("started_at")
    val startedAt: String? = null,

    @field:SerializedName("quotes")
    var quotes: QuotesEntity? = null,
)

data class QuotesEntity(

    @field:SerializedName("USD")
    val usd: USDEntity? = null,
)

data class USDEntity(

    @field:SerializedName("price")
    val price: Double? = null,
)
