package com.example.customviewwithoutcompose.data.network.entities.mappers

import com.example.customviewwithoutcompose.data.network.entities.CoinEntity
import com.example.customviewwithoutcompose.domain.models.CoinDomain

object CoinDomainMapper : FromEntityToDomainMapper<CoinEntity, CoinDomain> {

    override fun CoinEntity?.mapToDomain(): CoinDomain {
        return CoinDomain(
            id = this?.id ?: "",
            name = this?.name ?: "",
            symbol = this?.symbol ?: "",
            rank = this?.rank ?: 0,
            isNew = this?.isNew == true,
            isActive = this?.isActive == true,
            type = this?.type ?: "",
            startedAt = this?.startedAt ?: "",
            logo = this?.logo ?: "",
            description = this?.description ?: "",
            price = this?.quotes?.usd?.price ?: 0.0
        )
    }

    override fun mapToDomainList(list: List<CoinEntity?>?): List<CoinDomain> {
        return list.toDomainList()
    }
}