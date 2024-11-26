package com.verygoodsecurity.sdk.analytics

import com.verygoodsecurity.sdk.analytics.data.repository.AnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.DefaultAnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.Mapper
import com.verygoodsecurity.sdk.analytics.data.source.remote.DefaultAnalyticsRemoteDataSource
import com.verygoodsecurity.sdk.analytics.model.Event
import com.verygoodsecurity.sdk.analytics.model.Status
import com.verygoodsecurity.sdk.analytics.model.Upstream
import com.verygoodsecurity.sdk.analytics.utils.Session
import com.verygoodsecurity.sdk.analytics.utils.deviceInfo
import com.verygoodsecurity.sdk.analytics.utils.randomUUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import com.verygoodsecurity.sdk.analytics.data.repository.model.Event as EventModel

// TODO: Consider trackRequest/trackResponse/trackInit functions names
class AnalyticsManager internal constructor(
    private val vault: String,
    private val environment: String,
    private val source: String,
    private val sourceVersion: String,
    private val container: Container
) {

    private val scope = CoroutineScope(context = Job() + Dispatchers.Main)

    private val defaultEventParams: Map<String, Any> = getDefaultEventParams()

    private val repository: AnalyticsRepository = container.getAnalyticsRepository()

    constructor(vault: String, environment: String, source: String, sourceVersion: String) : this(
        vault = vault,
        environment = environment,
        source = source,
        sourceVersion = sourceVersion,
        container = Container()
    )

    fun capture(event: Event) {
        scope.launch(Dispatchers.IO) {
            repository.capture(EventModel(params = event.getParams() + defaultEventParams))
        }
    }

    fun cancelAll() {
        try {
            scope.coroutineContext.cancelChildren()
        } catch (_: IllegalStateException) {
        }
    }

    private fun getDefaultEventParams(): Map<String, Any> {
        val deviceInfo = deviceInfo()
        return mapOf(
            EventParams.VAULT_ID to vault,
            EventParams.ENVIRONMENT to environment,
            EventParams.SESSION_ID to Session.id,
            EventParams.FORM_ID to randomUUID(),
            EventParams.SOURCE to source,
            EventParams.SOURCE_VERSION to sourceVersion,
            EventParams.STATUS to Status.OK.capitalize(),
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