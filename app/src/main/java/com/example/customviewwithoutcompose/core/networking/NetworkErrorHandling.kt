package com.example.customviewwithoutcompose.core.networking

import android.util.Log
import androidx.annotation.CallSuper
import com.example.customviewwithoutcompose.utils.TAG
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response

internal interface ApiErrorHandler<T> {

    fun handleApiResponse(response: Response<T>): Result<T>
    fun handleApiList(response: Response<List<T>>, list: List<T>?): Result<List<T>>

    fun handleSuccess(response: Response<T>): Result<T> {
        return Result.success(checkNotNull(response.body()))
    }

    fun handleListSuccess(response: Response<List<T>>, list: List<T>): Result<List<T>> {
        return Result.success(list)
    }

    open class DefaultApiErrorHandler<T> : ApiErrorHandler<T> {

        @CallSuper
        override fun handleApiResponse(response: Response<T>): Result<T> {
            if (response.isSuccessful) {
                return handleSuccess(response)
            }

            val errors = response.extractApiErrors()

            return Result.failure(
                ApiException(
                    message = errors?.error,
                    code = response.code(),
                    type = errors?.type,
                    blockDuration = errors?.blockDuration
                )
            )
        }

        override fun handleApiList(response: Response<List<T>>, list: List<T>?): Result<List<T>> {
            if (response.isSuccessful && list != null) {
                return handleListSuccess(response, list)
            }

            val errors = response.extractApiErrors()

            return Result.failure(
                ApiException(
                    message = errors?.error,
                    code = response.code(),
                    type = errors?.type,
                    blockDuration = errors?.blockDuration
                )
            )
        }
    }
}

private fun <T> Response<T>.extractApiErrors(): ErrorsEntity? {
    val errorBodyJson = errorBody()?.charStream()?.use { it.readText() } ?: run {
        Log.w(TAG, "No error body")
        return null
    }

    return try {
        Gson().fromJson(errorBodyJson, ErrorsEntity::class.java)
    } catch (e: JsonSyntaxException) {
        Log.e(TAG, "error parsing failed: $errorBodyJson", e)
        null
    } catch (e: Throwable) {
        Log.e(TAG, "unknown error during error parsing: $errorBodyJson", e)
        null
    }
}