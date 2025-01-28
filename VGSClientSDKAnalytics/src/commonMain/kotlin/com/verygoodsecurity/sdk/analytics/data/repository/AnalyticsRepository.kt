package com.verygoodsecurity.sdk.analytics.data.repository

import com.verygoodsecurity.sdk.analytics.data.repository.model.Event

internal interface AnalyticsRepository {

    suspend fun capture(event: Event)
}