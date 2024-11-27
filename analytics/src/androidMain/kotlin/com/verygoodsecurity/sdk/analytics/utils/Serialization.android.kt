package com.verygoodsecurity.sdk.analytics.utils

import android.util.Base64
import com.verygoodsecurity.sdk.analytics.BuildConfig
import org.json.JSONObject

internal actual fun Map<String, Any>.toBase64Json(): String? {
    return try {
        val json = JSONObject(this).toString()
        if (BuildConfig.DEBUG) {
            println("DEBUG::android, toBase64Json, json = $json")
        }
        Base64.encodeToString(json.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    } catch (e: NullPointerException) {
        null
    }
}