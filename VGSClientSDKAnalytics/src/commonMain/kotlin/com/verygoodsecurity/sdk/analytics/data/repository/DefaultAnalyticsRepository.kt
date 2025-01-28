package com.verygoodsecurity.sdk.analytics.data.repository

import com.verygoodsecurity.sdk.analytics.data.repository.model.Event
import com.verygoodsecurity.sdk.analytics.data.source.remote.DefaultAnalyticsRemoteDataSource

internal class DefaultAnalyticsRepository(
    private val remoteDataSource: DefaultAnalyticsRemoteDataSource,
    private val mapper: Mapper
) : AnalyticsRepository {

    override suspend fun capture(event: Event) {
        // We can have local remote data source here, and store event locally before send it,
        // if we for example decide to optimize network requests
        remoteDataSource.capture(mapper.toDTO(event))
    }
}