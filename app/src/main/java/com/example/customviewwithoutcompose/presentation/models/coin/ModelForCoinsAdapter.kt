package com.example.customviewwithoutcompose.presentation.models.coin

data class ModelForCoinsAdapter(
    val customViewModel: ModelForCoinCustomView = ModelForCoinCustomView.DEFAULT,
    val isExpanded: Boolean = false
)