package com.verygoodsecurity.sdk.analytics.data.source.remote

import com.verygoodsecurity.sdk.analytics.data.source.remote.client.httpClient
import com.verygoodsecurity.sdk.analytics.data.source.remote.dto.Event
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType

private const val URL = "https://vgs-collect-keeper.apps.verygood.systems/vgs"

internal class DefaultAnalyticsRemoteDataSource : AnalyticsRemoteDataSource {

    private val client: HttpClient = httpClient {
        defaultRequest {
            url(URL)
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    override suspend fun capture(event: Event) {
        val body = FormDataContent(Parameters.build {
            this.append("tnt", event.tnt)
            this.append("name", "Debug")
        })
        client.post {
            setBody(body)
        }
    }
}