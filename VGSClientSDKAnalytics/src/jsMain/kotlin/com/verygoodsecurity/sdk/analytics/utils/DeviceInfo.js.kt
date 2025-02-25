package com.verygoodsecurity.sdk.analytics.utils

internal actual fun deviceInfo(): DeviceInfo {
    return DeviceInfo(
        platform = "JS",
        brand = "",
        model = "",
        osVersion = "",
    )
}