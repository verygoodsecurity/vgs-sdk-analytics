package com.verygoodsecurity.sdk.analytics.data

import com.verygoodsecurity.sdk.analytics.data.repository.AnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.DefaultAnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.Mapper
import com.verygoodsecurity.sdk.analytics.data.repository.model.Event
import com.verygoodsecurity.sdk.analytics.data.source.remote.DefaultAnalyticsRemoteDataSource

// TODO: Consider trackRequest/trackResponse/trackInit functions names
class AnalyticsManager internal constructor(private val container: Container) {

    constructor() : this(container = Container())

    private val repository: AnalyticsRepository = container.getAnalyticsRepository()

    suspend fun capture() {
        repository.capture(Event(tnt = ""))
    }
}

internal open class Container {

    open fun getAnalyticsRepository(): AnalyticsRepository {
        return DefaultAnalyticsRepository(
            remoteDataSource = DefaultAnalyticsRemoteDataSource(),
            mapper = Mapper()
        )
    }
}