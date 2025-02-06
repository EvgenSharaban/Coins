package com.example.customviewwithoutcompose.data.repositories

import com.example.customviewwithoutcompose.domain.repositories.StatisticRepository

class StatisticRepositoryImpl : StatisticRepository {

    override suspend fun getAmountOfDaysAppUsing(): Result<Int> {
        return Result.success(10)
    }

}