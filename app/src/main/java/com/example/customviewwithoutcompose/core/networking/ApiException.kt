package com.example.customviewwithoutcompose.core.networking

import java.io.IOException

class ApiException(
    message: String? = null,
    val code: Int,
    val type: String? = null,
    val blockDuration: String? = null
) : IOException(message) {

    override fun toString(): String {
        return "\n$message, \nStatus code: $code, \nType: $type, \nblock_duration: $blockDuration"
    }
}