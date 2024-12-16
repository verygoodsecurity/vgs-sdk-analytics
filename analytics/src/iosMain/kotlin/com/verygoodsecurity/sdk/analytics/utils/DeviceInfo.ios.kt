package com.verygoodsecurity.sdk.analytics.utils

import platform.UIKit.UIDevice

internal actual fun deviceInfo(): DeviceInfo {
    return DeviceInfo(
        platform = UIDevice.currentDevice.systemName,
        brand = UIDevice.currentDevice.model,
        model = UIDevice.currentDevice.name,
        osVersion = UIDevice.currentDevice.systemVersion,
    )
}