package com.verygoodsecurity.sdk.analytics.model

enum class VGSAnalyticsStatus {

    OK,
    FAILED,
    CANCELED;

    fun getAnalyticsName(): String {
        return name.lowercase().replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }
    }
}