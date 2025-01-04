package com.example.customviewwithoutcompose.core.networking

import android.util.Log
import com.example.customviewwithoutcompose.data.network.entities.mappers.FromEntityToDomainMapper
import com.example.customviewwithoutcompose.utils.TAG
import retrofit2.Response

suspend fun <T> safeApiCall(
    action: suspend () -> Response<T>
): Result<T> = safeApiCallInternal(
    action = action,
    apiErrorHandler = ApiErrorHandler.DefaultApiErrorHandler()
)

private suspend fun <T> safeApiCallInternal(
    apiErrorHandler: ApiErrorHandler<T>,
    action: suspend () -> Response<T>,
): Result<T> {
    return try {
        val response = action()
        apiErrorHandler.handleApiResponse(response)
    } catch (e: Throwable) {
        Log.e(TAG, "error during request: $e", e)
        Result.failure(e)
    }
}

suspend fun <T> safeApiCallList(
    action: suspend () -> Response<List<T>>
): Result<List<T>> = safeApiCallInternalList(
    action = action,
    apiErrorHandler = ApiErrorHandler.DefaultApiErrorHandler()
)

private suspend fun <T> safeApiCallInternalList(
    apiErrorHandler: ApiErrorHandler<T>,
    action: suspend () -> Response<List<T>>,
): Result<List<T>> {
    return try {
        val response = action()
        apiErrorHandler.handleApiList(response, response.body())
    } catch (e: Throwable) {
        Log.e(TAG, "error during request: $e", e)
        Result.failure(e)
    }
}

fun <R, T> Result<T>.toDomain(mapper: FromEntityToDomainMapper<T, R>): Result<R> {
    return map { value ->
        mapper.mapToDomainModel(value)
    }
}

fun <R, T> Result<List<T>>.toDomainList(mapper: FromEntityToDomainMapper<T, R>): Result<List<R>> {
    return map { value ->
        mapper.mapToDomainList(value)
    }
}


