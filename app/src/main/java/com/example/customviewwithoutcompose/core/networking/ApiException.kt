package com.example.customviewwithoutcompose.core.networking

import java.io.IOException

class ApiException(
    message: String? = null,
    val code: Int,
) : IOException(message) {

    override fun toString(): String {
        return "$message, Status code: $code"
    }
}