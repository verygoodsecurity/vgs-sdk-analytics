package com.verygoodsecurity.sdk.analytics.model

enum class Upstream {

    TOKENIZATION,
    CUSTOM;

    fun getAnalyticsName() = name.lowercase()

    companion object {

        fun get(isTokenization: Boolean): Upstream {
            return if (isTokenization) {
                TOKENIZATION
            } else {
                CUSTOM
            }
        }
    }
}