package com.verygoodsecurity.sdk.analytics.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSJSONWritingPrettyPrinted
import platform.Foundation.base64Encoding
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
internal actual fun Map<String, Any>.toBase64Json(): String? {
    val json = NSJSONSerialization.dataWithJSONObject(
        obj = this,
        options = NSJSONWritingPrettyPrinted,
        error = null
    )
    if (Platform.isDebugBinary) {
        println("DEBUG::iOS, toBase64Json, json = ${json.toString()}")
    }
    return json?.base64Encoding()
}