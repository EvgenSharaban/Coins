package com.example.customviewwithoutcompose.core.other

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale


fun Double.roundTo(numbersAfterPoint: Int): Double {
    return String.format(Locale.US, "%.${numbersAfterPoint}f", this).toDouble()
}

fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.US)
    if (date.isEmpty()) {
        Log.w(TAG, "formatDate: empty string to format")
        return ""
    }
    return try {
        outputFormat.format(inputFormat.parse(date) ?: date)
    } catch (e: Throwable) {
        Log.w(TAG, "formatDate: failed, \nerror = $e")
        ""
    }
}

fun Int.fromDpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density).toInt()
}