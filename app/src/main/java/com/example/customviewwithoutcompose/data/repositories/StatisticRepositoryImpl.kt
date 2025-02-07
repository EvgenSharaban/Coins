package com.example.customviewwithoutcompose.data.repositories

import com.example.customviewwithoutcompose.domain.repositories.StatisticRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor() : StatisticRepository {

    override suspend fun getAmountOfDaysAppUsing(): Result<Int> {
        delay(3000)
        return Result.success(10)
//        return Result.failure(Exception(FAILURE_VALUE))
    }

    override suspend fun getTotalItemsCount(): Result<Int> {
        delay(1000)
        return Result.success(15)
//        return Result.failure(Exception(FAILURE_VALUE))
    }

}