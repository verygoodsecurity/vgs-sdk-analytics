package com.verygoodsecurity.sdk.analytics.utils

internal data class DeviceInfo(
    val platform: String,
    val brand: String,
    val model: String,
    val osVersion: String
)

internal expect fun deviceInfo(): DeviceInfo