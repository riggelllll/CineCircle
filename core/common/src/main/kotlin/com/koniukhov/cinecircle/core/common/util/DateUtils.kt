package com.koniukhov.cinecircle.core.common.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun formatDate(date: String, inputPattern: String = "yyyy-MM-dd"): String {
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            val outputFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault())
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            Timber.d(e)
            date
        }
    }
}