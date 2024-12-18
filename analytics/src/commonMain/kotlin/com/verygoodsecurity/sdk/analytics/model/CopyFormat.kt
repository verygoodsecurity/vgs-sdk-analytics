package com.verygoodsecurity.sdk.analytics.model

enum class CopyFormat {

    RAW,
    FORMATTED;

    fun getAnalyticsName() = name.lowercase()
}