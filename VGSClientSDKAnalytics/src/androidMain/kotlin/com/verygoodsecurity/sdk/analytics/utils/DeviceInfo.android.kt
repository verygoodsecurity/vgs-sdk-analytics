package com.verygoodsecurity.sdk.analytics.utils

import android.os.Build

private const val PLATFORM = "android"

internal actual fun deviceInfo() = DeviceInfo(
    platform = PLATFORM,
    brand = Build.BRAND,
    model = Build.MODEL,
    osVersion = Build.VERSION.SDK_INT.toString(),
)