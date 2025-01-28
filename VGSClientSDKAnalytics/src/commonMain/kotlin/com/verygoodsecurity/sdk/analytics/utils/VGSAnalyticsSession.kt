package com.verygoodsecurity.sdk.analytics.utils

object VGSAnalyticsSession {

    val id = randomUUID()
}

internal expect fun randomUUID(): String