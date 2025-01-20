package com.example.customviewwithoutcompose.data.local.room.entities

import com.example.customviewwithoutcompose.domain.models.CoinDomain

object CoinDataBaseMapper {

    fun CoinDomain.mapToLocalEntity(): CoinLocalEntity {
        return CoinLocalEntity(
            id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            isNew = this.isNew,
            isActive = this.isActive,
            type = this.type,
            startedAt = this.startedAt,
            logo = this.logo,
            description = this.description,
            price = this.price
        )
    }

    fun List<CoinDomain>.mapToLocalEntityList(): List<CoinLocalEntity> = this.map { it.mapToLocalEntity() }

    fun CoinLocalEntity.mapToDomain(): CoinDomain {
        return CoinDomain(
            id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            isNew = this.isNew,
            isActive = this.isActive,
            type = this.type,
            startedAt = this.startedAt,
            logo = this.logo,
            description = this.description,
            price = this.price
        )
    }

    fun List<CoinLocalEntity>.mapToDomainList(): List<CoinDomain> = this.map { it.mapToDomain() }

}