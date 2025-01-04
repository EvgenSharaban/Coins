package com.example.customviewwithoutcompose.domain.models

import androidx.annotation.StyleRes
import com.example.customviewwithoutcompose.R

data class ModelForCustomView(
    val id: String,
    val rank: Int,
    @StyleRes val rankTextAppearance: Int = R.style.RankTextAppearance,

    val nameText: String,
    val descriptionText: String,
    val creationDate: String,
    var logo: String,

    val shortNameText: String,
    @StyleRes val shortNameTextAppearance: Int = R.style.ShortNameTextAppearance,

    val type: String,
    val isActive: Boolean
) {

    companion object {

        val DEFAULT = ModelForCustomView(
            id = "",
            rank = 0,
            descriptionText = "",
            nameText = "",
            creationDate = "",
            logo = "",
            shortNameText = "",
            type = "",
            isActive = false
        )
    }
}
