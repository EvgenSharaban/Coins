package com.example.customviewwithoutcompose.presentation.uimodels

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes

data class ModelForCustomView(

    val rankText: String,
    @StyleRes val rankTextAppearance: Int,
    @ColorInt val rankBackgroundColor: Int,

    val nameText: String,
    val descriptionText: String,
    val creationDate: String,
    @DrawableRes val logo: Int,

    val shortNameText: String,
    @StyleRes val shortNameTextAppearance: Int,
    @ColorInt val shortNameBackgroundColor: Int
) {

    companion object {

        val DEFAULT = ModelForCustomView(
            rankText = "",
            descriptionText = "",
            rankTextAppearance = 0,
            rankBackgroundColor = 0,
            nameText = "",
            creationDate = "",
            logo = 0,
            shortNameText = "",
            shortNameTextAppearance = 0,
            shortNameBackgroundColor = 0
        )
    }
}
