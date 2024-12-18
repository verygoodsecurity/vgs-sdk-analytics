package com.verygoodsecurity.sdk.analytics.model

enum class MappingPolicy(internal val analyticsValue: String) {

    NESTED_JSON(analyticsValue = "nestedJson"),
    FLAT_JSON(analyticsValue = "flatJson"),
    NESTED_JSON_ARRAYS_MERGE(analyticsValue = "nestedJsonArrayMerge"),
    NESTED_JSON_ARRAYS_OVERWRITE(analyticsValue = "nestedJsonArrayOverwrite")
}