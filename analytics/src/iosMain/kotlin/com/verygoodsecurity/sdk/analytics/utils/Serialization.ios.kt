package com.verygoodsecurity.sdk.analytics.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSJSONWritingPrettyPrinted
import platform.Foundation.base64Encoding

@OptIn(ExperimentalForeignApi::class)
internal actual fun Map<String, Any>.toBase64Json(): String? {
    val json = NSJSONSerialization.dataWithJSONObject(
        obj = this,
        options = NSJSONWritingPrettyPrinted,
        error = null
    )
    return json?.base64Encoding()
}