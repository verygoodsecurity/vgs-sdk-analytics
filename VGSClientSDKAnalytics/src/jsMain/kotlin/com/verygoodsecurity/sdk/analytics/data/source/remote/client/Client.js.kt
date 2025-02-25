package com.verygoodsecurity.sdk.analytics.data.source.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.JsClient

internal actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(JsClient()) {
    config(this)
}