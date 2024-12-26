package com.example.customviewwithoutcompose.models

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class CustomViewModel(

    val rankText: String,
    val rankTextSize: Int,
    @ColorInt val rankBackgroundColor: Int,

    val nameText: String,
    val creationDate:String,
    @DrawableRes val logo: Int,

    val shortNameText: String,
    val shortNameTextSize: Int,
    @ColorInt val shortNameBackgroundColor: Int
) {

    companion object {

        val DEFAULT = CustomViewModel(
            rankText = "",
            rankTextSize = 0,
            rankBackgroundColor = 0,
            nameText = "",
            creationDate = "",
            logo = 0,
            shortNameText = "",
            shortNameTextSize = 0,
            shortNameBackgroundColor = 0
        )
    }
}
