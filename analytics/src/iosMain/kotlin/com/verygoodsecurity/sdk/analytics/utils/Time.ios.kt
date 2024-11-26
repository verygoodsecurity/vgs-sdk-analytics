package com.verygoodsecurity.sdk.analytics.utils

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

private const val MILLIS_IN_SECOND = 1000

actual fun currentTimeMillis(): Long  = NSDate().timeIntervalSince1970.toLong() * MILLIS_IN_SECOND