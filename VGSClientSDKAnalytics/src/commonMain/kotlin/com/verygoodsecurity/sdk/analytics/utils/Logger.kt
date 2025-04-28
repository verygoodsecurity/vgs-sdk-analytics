@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "unused")

package com.verygoodsecurity.sdk.analytics.utils

const val TAG = "VGSAnalytics"

expect object Logger {

    fun e(tag: String = TAG, message: String, throwable: Throwable? = null)

    fun d(tag: String = TAG, message: String)

    fun i(tag: String = TAG, message: String)

    fun w(tag: String = TAG, message: String)
}