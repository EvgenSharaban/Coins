package com.example.customviewwithoutcompose.data.repositories

import android.content.Context
import com.example.customviewwithoutcompose.R
import com.example.customviewwithoutcompose.domain.repositories.StatisticRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : StatisticRepository {

    override suspend fun getAmountOfDaysAppUsing(): Result<Int> {
        delay(3000)
        return Result.success(10)
//        return Result.failure(Exception("gjhghgjhfjg"))
    }

    override suspend fun getTotalItemsCount(): Result<Int> {
        delay(2000)
//        return Result.success(15)
        return Result.failure(Exception(context.getString(R.string.unknown_error)))
    }

}