package com.verygoodsecurity.sdk.analytics.data.source.remote

import com.verygoodsecurity.sdk.analytics.data.source.remote.client.httpClient
import com.verygoodsecurity.sdk.analytics.data.source.remote.dto.Event
import com.verygoodsecurity.sdk.analytics.utils.toBase64Json
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val DEFAULT_TIMEOUT = 60000L

private const val URL = "https://vgs-collect-keeper.apps.verygood.systems/vgs"

internal class DefaultAnalyticsRemoteDataSource : AnalyticsRemoteDataSource {

    private val client: HttpClient = httpClient {
        install(HttpTimeout) {
            connectTimeoutMillis = DEFAULT_TIMEOUT
            socketTimeoutMillis = DEFAULT_TIMEOUT
            requestTimeoutMillis = DEFAULT_TIMEOUT
        }
        defaultRequest {
            url(URL)
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun capture(event: Event) {
        val body = event.params.toBase64Json()
        client.post {
            setBody(body)
        }
    }
}