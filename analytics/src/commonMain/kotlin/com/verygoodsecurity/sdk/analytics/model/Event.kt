package com.verygoodsecurity.sdk.analytics.model

internal sealed class Event {

    protected abstract val type: String

    protected abstract val attributes: Map<String, Any>
}