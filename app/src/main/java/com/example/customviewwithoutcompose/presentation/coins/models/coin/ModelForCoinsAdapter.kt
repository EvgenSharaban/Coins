package com.example.customviewwithoutcompose.presentation.coins.models.coin

data class ModelForCoinsAdapter(
    val customViewModel: ModelForCoinCustomView = ModelForCoinCustomView.DEFAULT,
    val isExpanded: Boolean = false
)