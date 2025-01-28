package com.verygoodsecurity.sdk.analytics.utils

import java.util.UUID

actual fun randomUUID(): String = UUID.randomUUID().toString()