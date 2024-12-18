package com.verygoodsecurity.sdk.analytics.model

enum class Status {

    OK,
    FAILED, // TODO: Consider rename this state to FAIL or CANCEL to CANCELED
    CANCELED;

    fun getAnalyticsName(): String {
        return name.lowercase().replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }
    }
}