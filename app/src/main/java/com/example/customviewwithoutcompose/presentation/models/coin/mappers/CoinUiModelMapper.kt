package com.example.customviewwithoutcompose.presentation.models.coin.mappers

import com.example.customviewwithoutcompose.data.local.room.entities.CoinRoomEntity
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinCustomView

object CoinUiModelMapper {

    fun CoinRoomEntity.mapToUiModel(): ModelForCoinCustomView {
        return ModelForCoinCustomView(
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