package com.example.customviewwithoutcompose.presentation.models.coin

data class ModelForAdapter(
    val customViewModel: ModelForCoinCustomView = ModelForCoinCustomView.DEFAULT,
    val isExpanded: Boolean = false
)