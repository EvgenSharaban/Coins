package com.example.customviewwithoutcompose.domain.repositories

interface StatisticRepository {

    suspend fun getAmountOfDaysAppUsing(): Result<Int>
    suspend fun getTotalItemsCount(): Result<Int>

}