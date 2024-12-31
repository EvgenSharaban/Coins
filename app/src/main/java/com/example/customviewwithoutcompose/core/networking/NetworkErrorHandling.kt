package com.example.customviewwithoutcompose.core.networking

import androidx.annotation.CallSuper
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

            return Result.failure(
                ApiException(
                    message = response.errorBody().toString(),
                    code = response.code()
                )
            )
        }

        override fun handleApiList(response: Response<List<T>>, list: List<T>?): Result<List<T>> {
            if (response.isSuccessful && list != null) {
                return handleListSuccess(response, list)
            }

            return Result.failure(
                ApiException(
                    message = response.errorBody().toString(),
                    code = response.code()
                )
            )
        }
    }
}