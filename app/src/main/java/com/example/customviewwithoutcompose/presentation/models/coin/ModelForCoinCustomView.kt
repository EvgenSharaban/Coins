package com.example.customviewwithoutcompose.presentation.models.coin

import androidx.annotation.StyleRes
import com.example.customviewwithoutcompose.R

data class ModelForCoinCustomView(
    val id: String,
    val rank: Int,
    @StyleRes val rankTextAppearance: Int = R.style.RankTextAppearance,

    val nameText: String,
    val descriptionText: String,
    val creationDate: String,
    val logo: String,

    val shortNameText: String,
    @StyleRes val shortNameTextAppearance: Int = R.style.ShortNameTextAppearance,

    val type: String,
    val isActive: Boolean,
    val price: Double
) {

    companion object {

        val DEFAULT = ModelForCoinCustomView(
            id = "",
            rank = 0,
            descriptionText = "",
            nameText = "",
            creationDate = "",
            logo = "",
            shortNameText = "",
            type = "",
            isActive = false,
            price = 0.0
        )
    }
}
