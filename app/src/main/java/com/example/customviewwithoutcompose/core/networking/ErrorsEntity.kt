package com.example.customviewwithoutcompose.core.networking

import com.google.gson.annotations.SerializedName

data class ErrorsEntity(
    @SerializedName("type") val type: String? = null,
    @SerializedName("hard_limit") val hardLimit: String? = null,
    @SerializedName("soft_limit") val softLimit: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("block_duration") val blockDuration: String? = null
)