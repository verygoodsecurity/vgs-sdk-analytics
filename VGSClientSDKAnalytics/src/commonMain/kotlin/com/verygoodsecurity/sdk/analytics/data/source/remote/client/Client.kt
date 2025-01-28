package com.verygoodsecurity.sdk.analytics.data.source.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

internal expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient