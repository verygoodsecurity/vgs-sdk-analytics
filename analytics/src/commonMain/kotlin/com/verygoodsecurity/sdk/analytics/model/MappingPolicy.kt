package com.verygoodsecurity.sdk.analytics.model

enum class MappingPolicy {

    NESTED_JSON,
    FLAT_JSON,
    NESTED_JSON_ARRAYS_MERGE,
    NESTED_JSON_ARRAYS_OVERWRITE;

    fun getAnalyticsName() = name.lowercase()
}