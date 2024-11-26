package com.verygoodsecurity.sdk.analytics

import com.verygoodsecurity.sdk.analytics.data.repository.AnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.DefaultAnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.Mapper
import com.verygoodsecurity.sdk.analytics.data.repository.model.Event
import com.verygoodsecurity.sdk.analytics.data.source.remote.DefaultAnalyticsRemoteDataSource
import com.verygoodsecurity.sdk.analytics.utils.Session
import com.verygoodsecurity.sdk.analytics.utils.currentTimeMillis
import com.verygoodsecurity.sdk.analytics.utils.deviceInfo
import com.verygoodsecurity.sdk.analytics.utils.randomUUID

// TODO: Consider trackRequest/trackResponse/trackInit functions names
class AnalyticsManager internal constructor(
    private val vault: String,
    private val environment: String,
    private val source: String,
    private val sourceVersion: String,
    private val container: Container
) {

    private val defaultEventAttributes: Map<String, Any> = generateDefaultEventParams()

    private val repository: AnalyticsRepository = container.getAnalyticsRepository()

    constructor(vault: String, environment: String, source: String, sourceVersion: String) : this(
        vault = vault,
        environment = environment,
        source = source,
        sourceVersion = sourceVersion,
        container = Container()
    )

    suspend fun capture() {
        repository.capture(Event(tnt = ""))
    }

    private fun generateDefaultEventParams(): Map<String, Any> {
        val deviceInfo = deviceInfo()
        return mapOf(
            EventParams.VAULT_ID to vault,
            EventParams.ENVIRONMENT to environment,
            EventParams.SESSION_ID to Session.id,
            EventParams.FORM_ID to randomUUID(),
            EventParams.SOURCE to source,
            EventParams.SOURCE_VERSION to sourceVersion,
            EventParams.TIMESTAMP to currentTimeMillis(),
            EventParams.STATUS to EventStatus.OK.value,
            EventParams.UA to mapOf(
                EventParams.DEVICE_PLATFORM to deviceInfo.platform,
                EventParams.DEVICE_BRAND to deviceInfo.brand,
                EventParams.DEVICE_MODEL to deviceInfo.model,
                EventParams.DEVICE_OS_VERSION to deviceInfo.osVersion,
            )
        )
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