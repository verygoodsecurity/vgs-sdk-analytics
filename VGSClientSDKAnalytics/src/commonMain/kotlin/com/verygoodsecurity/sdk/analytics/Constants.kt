package com.verygoodsecurity.sdk.analytics

internal object EventTypes {

    const val FIELD_ATTACH = "Init"
    const val FIELD_DETACH = "UnsubscribeField"
    const val CNAME = "HostNameValidation"
    const val REQUEST = "BeforeSubmit"
    const val RESPONSE = "Submit"
    const val AUTOFILL = "Autofill"
    const val ATTACH_FILE = "AttachFile"
    const val SCAN = "Scan"
    const val COPY_TO_CLIPBOARD = "CopyToClipboard"
    const val SET_SECURE_TEXT_RANGE = "SetSecureTextRange"
    const val CONTENT_RENDERING = "ContentRendering"
    const val CONTENT_SHARING = "ContentSharing"
}

internal object EventParams {

    const val TYPE = "type"
    const val VAULT_ID = "tnt"
    const val ENVIRONMENT = "env"
    const val SOURCE_VERSION = "version"
    const val SESSION_ID = "vgsSessionId"
    const val DEPENDENCY_MANAGER = "dependencyManager"
    const val FORM_ID = "formId"
    const val SOURCE = "source"
    const val TIMESTAMP = "localTimestamp"
    const val STATUS = "status"
    const val CODE = "statusCode"
    const val UA = "ua"
    const val DEVICE_PLATFORM = "platform"
    const val DEVICE_BRAND = "device"
    const val DEVICE_MODEL = "deviceModel"
    const val DEVICE_OS_VERSION = "osVersion"
    const val FIELD_TYPE = "fieldType"
    const val CONTENT_PATH = "contentPath"
    const val UI = "ui"
    const val HOSTNAME = "hostname"
    const val SCAN_ID = "scanId"
    const val SCANNER_TYPE = "scannerType"
    const val SCAN_DETAILS = "details"
    const val UPSTREAM = "upstream"
    const val CONTENT = "content"
    const val ERROR = "error"
    const val ERROR_CODE = "errorCode"
    const val COPY_FORMAT = "copyFormat"
    const val LATENCY = "latency"
}

internal object EventValues {

    const val CUSTOM_HOSTNAME = "customHostname"
    const val CUSTOM_HEADER = "customHeader"
    const val CUSTOM_DATA = "customData"
    const val FILES = "file"
    const val FIELDS = "textField"
    const val PDF = "pdf"
}
