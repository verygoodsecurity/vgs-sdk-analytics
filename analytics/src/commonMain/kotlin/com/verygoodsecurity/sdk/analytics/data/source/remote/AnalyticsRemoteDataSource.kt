package com.verygoodsecurity.sdk.analytics.data.source.remote

import com.verygoodsecurity.sdk.analytics.data.source.remote.dto.Event

internal interface AnalyticsRemoteDataSource {

    suspend fun capture(event: Event)
}