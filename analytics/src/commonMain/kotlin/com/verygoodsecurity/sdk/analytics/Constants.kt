package com.verygoodsecurity.sdk.analytics

internal data object EventParams {

    const val VAULT_ID = "tnt"
    const val ENVIRONMENT = "env"
    const val SOURCE_VERSION = "version"
    const val SESSION_ID = "vgsShowSessionId"
    const val FORM_ID = "formId"
    const val SOURCE = "source"
    const val TIMESTAMP = "localTimestamp"
    const val STATUS = "status"
    const val UA = "ua"
    const val DEVICE_PLATFORM = "platform"
    const val DEVICE_BRAND = "device"
    const val DEVICE_MODEL = "deviceModel"
    const val DEVICE_OS_VERSION = "osVersion"
}

internal enum class EventStatus(val value: String) {

    OK("Ok"),
    FAILED("Failed")
}
