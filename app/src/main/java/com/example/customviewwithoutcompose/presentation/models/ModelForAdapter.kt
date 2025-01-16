package com.example.customviewwithoutcompose.presentation.models

data class ModelForAdapter(
    val customViewModel: ModelForCustomView = ModelForCustomView.DEFAULT,
    val isExpanded: Boolean = false
)