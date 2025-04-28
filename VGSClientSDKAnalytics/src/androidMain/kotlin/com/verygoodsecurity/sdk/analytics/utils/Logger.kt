@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "unused")

package com.verygoodsecurity.sdk.analytics.utils

import android.util.Log

actual object Logger {

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        throwable?.let { t ->
            Log.e(tag, message, t)
        } ?: Log.e(tag, message)
    }

    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    actual fun w(tag: String, message: String) {
        Log.w(tag, message)
    }
}