package com.verygoodsecurity.sdk.analytics.model

enum class Status {

    OK,
    FAILED,
    CANCEL;

    fun capitalize(): String {
        return name.lowercase().replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }
    }
}