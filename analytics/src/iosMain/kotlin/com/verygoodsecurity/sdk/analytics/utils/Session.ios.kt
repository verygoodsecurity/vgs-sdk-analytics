package com.verygoodsecurity.sdk.analytics.utils

import platform.Foundation.NSUUID

actual fun randomUUID() = NSUUID().UUIDString()