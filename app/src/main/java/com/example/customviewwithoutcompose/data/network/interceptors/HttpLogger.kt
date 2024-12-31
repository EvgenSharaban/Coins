package com.example.customviewwithoutcompose.data.network.interceptors

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class HttpLogger @Inject constructor() : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        Log.i("HttpLogger", message)
    }
}