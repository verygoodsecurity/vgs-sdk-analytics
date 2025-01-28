package com.verygoodsecurity.sdk.analytics.model

import com.verygoodsecurity.sdk.analytics.EventParams

data class DefaultEventParams(
    private val vault: String,
    private val environment: String,
    private val source: String,
    private val sourceVersion: String,
    private val dependencyManager: String,
) {

    fun getParams(): Map<String, Any> = mapOf(
        EventParams.VAULT_ID to vault,
        EventParams.ENVIRONMENT to environment,
        EventParams.SOURCE to source,
        EventParams.SOURCE_VERSION to sourceVersion,
        EventParams.DEPENDENCY_MANAGER to dependencyManager
    )
}