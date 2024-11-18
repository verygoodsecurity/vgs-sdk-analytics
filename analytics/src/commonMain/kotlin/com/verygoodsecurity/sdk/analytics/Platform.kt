package com.verygoodsecurity.sdk.analytics

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform