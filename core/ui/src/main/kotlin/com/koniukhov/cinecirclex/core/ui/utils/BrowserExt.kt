package com.koniukhov.cinecirclex.core.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import timber.log.Timber

fun Context.openWebsite(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    } catch (e: Exception) {
        Timber.Forest.e(e, "Failed to open website: $url")
    }
}