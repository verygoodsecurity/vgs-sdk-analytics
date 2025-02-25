package com.verygoodsecurity.sdk.analytics

@OptIn(ExperimentalJsExport::class)
@JsExport
actual fun greetings(): String? {
    return "Hello, it's JS!"
}