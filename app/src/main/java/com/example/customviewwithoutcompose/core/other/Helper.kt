package com.example.customviewwithoutcompose.core.other

import java.text.SimpleDateFormat
import java.util.Locale


fun Double.roundTo(numbersAfterPoint: Int): Double {
    return String.format(Locale.US, "%.${numbersAfterPoint}f", this).toDouble()
}

fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.US)
    return outputFormat.format(inputFormat.parse(date) ?: date)
}