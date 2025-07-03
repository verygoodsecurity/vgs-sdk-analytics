package com.verygoodsecurity.sdk.analytics.model

enum class VGSAnalyticsUpstream {

    TOKENIZATION,
    CUSTOM,
    CMP;

    fun getAnalyticsName() = name.lowercase()
}