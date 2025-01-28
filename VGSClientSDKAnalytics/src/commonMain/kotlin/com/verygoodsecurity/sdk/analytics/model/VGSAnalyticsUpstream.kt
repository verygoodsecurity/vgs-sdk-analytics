package com.verygoodsecurity.sdk.analytics.model

enum class VGSAnalyticsUpstream {

    TOKENIZATION,
    CUSTOM;

    fun getAnalyticsName() = name.lowercase()

    companion object {

        fun get(isTokenization: Boolean): VGSAnalyticsUpstream {
            return if (isTokenization) {
                TOKENIZATION
            } else {
                CUSTOM
            }
        }
    }
}