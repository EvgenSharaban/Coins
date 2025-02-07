package com.example.customviewwithoutcompose.data.repositories

import com.example.customviewwithoutcompose.domain.repositories.StatisticRepository
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor() : StatisticRepository {

    override suspend fun getAmountOfDaysAppUsing(): Result<Int> {
        return Result.success(10)
        //        return Result.failure(Exception("-"))
    }

    override suspend fun getTotalItemsCount(): Result<Int> {
        return Result.success(15)
        //        return Result.failure(Exception("-"))
    }

}