package com.verygoodsecurity.sdk.analytics

import com.verygoodsecurity.sdk.analytics.data.repository.AnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.DefaultAnalyticsRepository
import com.verygoodsecurity.sdk.analytics.data.repository.Mapper
import com.verygoodsecurity.sdk.analytics.data.source.remote.DefaultAnalyticsRemoteDataSource
import com.verygoodsecurity.sdk.analytics.model.VGSAnalyticsEvent
import com.verygoodsecurity.sdk.analytics.model.VGSAnalyticsStatus
import com.verygoodsecurity.sdk.analytics.utils.VGSAnalyticsSession
import com.verygoodsecurity.sdk.analytics.utils.deviceInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import com.verygoodsecurity.sdk.analytics.data.repository.model.Event as EventModel

class VGSSharedAnalyticsManager internal constructor(
    private val source: String,
    private val sourceVersion: String,
    private val dependencyManager: String,
    private val provider: Provider
) {

    private var isEnabled: Boolean = true

    private val scope: CoroutineScope = provider.scope

    private val dispatcher: CoroutineDispatcher = provider.dispatcher

    private val repository: AnalyticsRepository = provider.getAnalyticsRepository()

    private val defaultEventParams: Map<String, Any> = provider.getDefaultEventParams(
        source = source, sourceVersion = sourceVersion, dependencyManager = dependencyManager
    )

    constructor(
        source: String,
        sourceVersion: String,
        dependencyManager: String,
    ) : this(
        source = source,
        sourceVersion = sourceVersion,
        dependencyManager = dependencyManager,
        provider = Provider()
    )

    fun getIsEnabled() = isEnabled

    fun setIsEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }

    fun capture(vault: String, environment: String, formId: String, event: VGSAnalyticsEvent) {
        if (!isEnabled) return
        val params = (this@VGSSharedAnalyticsManager.defaultEventParams + event.getParams()).toMutableMap()
        params[EventParams.VAULT_ID] = vault
        params[EventParams.ENVIRONMENT] = environment
        params[EventParams.FORM_ID] = formId
        scope.launch(dispatcher) {
            repository.capture(
                EventModel(params = params)
            )
        }
    }

    fun cancelAll() {
        try {
            scope.coroutineContext.cancelChildren()
        } catch (_: IllegalStateException) {
        }
    }
}

internal open class Provider {

    open val scope: CoroutineScope
        get() = CoroutineScope(context = Job() + Dispatchers.Main)

    open val dispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    open fun getAnalyticsRepository(): AnalyticsRepository {
        return DefaultAnalyticsRepository(
            remoteDataSource = DefaultAnalyticsRemoteDataSource(), mapper = Mapper()
        )
    }

    open fun getDefaultEventParams(
        source: String,
        sourceVersion: String,
        dependencyManager: String,
    ): Map<String, Any> {
        val deviceInfo = deviceInfo()
        return mapOf(
            EventParams.SESSION_ID to VGSAnalyticsSession.id,
            EventParams.SOURCE to source,
            EventParams.SOURCE_VERSION to sourceVersion,
            EventParams.DEPENDENCY_MANAGER to dependencyManager,
            EventParams.STATUS to VGSAnalyticsStatus.OK.getAnalyticsName(),
            EventParams.UA to mapOf(
                EventParams.DEVICE_PLATFORM to deviceInfo.platform,
                EventParams.DEVICE_BRAND to deviceInfo.brand,
                EventParams.DEVICE_MODEL to deviceInfo.model,
                EventParams.DEVICE_OS_VERSION to deviceInfo.osVersion,
            )
        )
    }
}