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
        val delay = 3000L
        delay(delay)
//        return Result.success(10)
        return fakeFailure(delay)
    }

    override suspend fun getTotalItemsCount(): Result<Int> {
        val delay = 2000L
        delay(delay)
//        return Result.success(15)
        return fakeFailure(delay)
    }

    private fun <R> fakeFailure(delay: Long): Result<R> {
        return Result.failure(Exception(context.getString(R.string.unknown_error) + ", delay = $delay"))
    }

}