package com.verygoodsecurity.sdk.analytics.model

enum class VGSAnalyticsCopyFormat {

    RAW,
    FORMATTED;

    fun getAnalyticsName() = name.lowercase()
}