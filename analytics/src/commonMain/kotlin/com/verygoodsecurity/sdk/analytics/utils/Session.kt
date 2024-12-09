package com.verygoodsecurity.sdk.analytics.utils

internal object Session {

    val id = randomUUID()
}

internal expect fun randomUUID(): String