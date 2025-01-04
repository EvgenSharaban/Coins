package com.example.customviewwithoutcompose.data.network.entities.mappers

import com.example.customviewwithoutcompose.data.network.entities.CoinEntity
import com.example.customviewwithoutcompose.domain.models.ModelForCustomView

object CoinMapper : FromEntityToDomainMapper<CoinEntity, ModelForCustomView> {

    override fun CoinEntity?.mapToDomain(): ModelForCustomView {
        return ModelForCustomView(
            id = this?.id ?: "",
            rank = this?.rank ?: 0,
            nameText = this?.name ?: "",
            descriptionText = this?.description ?: "",
            creationDate = this?.startedAt ?: "",
            logo = this?.logo ?: "",
            shortNameText = this?.symbol ?: "",
            type = this?.type ?: "",
            isActive = this?.isActive == true
        )
    }

    override fun mapToDomainList(list: List<CoinEntity?>?): List<ModelForCustomView> {
        return list.toDomainList()
    }
}