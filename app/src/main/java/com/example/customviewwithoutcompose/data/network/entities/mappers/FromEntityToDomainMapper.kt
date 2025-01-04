package com.example.customviewwithoutcompose.data.network.entities.mappers

interface FromEntityToDomainMapper<Entity, Domain> {

    fun Entity?.mapToDomain(): Domain

    fun mapToDomainList(list: List<Entity?>?): List<Domain> {
        return list.toDomainList()
    }

    fun mapToDomainModel(entity: Entity?): Domain {
        return entity.mapToDomain()
    }

    fun List<Entity?>?.toDomainList(): List<Domain> {
        return safeListResult(this).map {
            it.mapToDomain()
        }
    }

    private fun <T> safeListResult(list: List<T?>?): List<T> {
        if (list.isNullOrEmpty())
            return emptyList()
        return list.filterNotNull()
    }
}

