package com.example.customviewwithoutcompose.presentation.uimodels.mappers

import com.example.customviewwithoutcompose.domain.models.CoinDomain
import com.example.customviewwithoutcompose.presentation.uimodels.ModelForCustomView

object CoinUiModelMapper {

    fun CoinDomain.mapToUiModel(): ModelForCustomView {
        return ModelForCustomView(
            id = this.id,
            rank = this.rank,
            nameText = this.name,
            descriptionText = this.description,
            creationDate = this.startedAt,
            logo = this.logo,
            shortNameText = this.symbol,
            type = this.type,
            isActive = this.isActive,
            price = this.price
        )
    }

}