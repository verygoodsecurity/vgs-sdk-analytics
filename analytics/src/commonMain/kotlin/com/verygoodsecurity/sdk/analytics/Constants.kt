package com.verygoodsecurity.sdk.analytics

internal object EventTypes {

    const val FIELD_INIT = "Init"
    const val CNAME = "HostNameValidation"
    const val REQUEST = "BeforeSubmit"
    const val RESPONSE = "Submit"
    const val AUTOFILL = "Autofill"
    const val ATTACH_FILE = "AttachFile"
    const val SCAN = "Scan"
}

internal object EventParams {

    const val TYPE = "type"
    const val VAULT_ID = "tnt"
    const val ENVIRONMENT = "env"
    const val SOURCE_VERSION = "version"
    const val SESSION_ID = "vgsSessionId"
    const val FORM_ID = "formId"
    const val SOURCE = "source"
    const val TIMESTAMP = "localTimestamp"
    const val STATUS = "status"
    const val STATUS_CODE = "statusCode"
    const val UA = "ua"
    const val DEVICE_PLATFORM = "platform"
    const val DEVICE_BRAND = "device"
    const val DEVICE_MODEL = "deviceModel"
    const val DEVICE_OS_VERSION = "osVersion"
    const val FIELD_TYPE = "field"
    const val CONTENT_PATH = "contentPath"
    const val HOSTNAME = "hostname"
    const val SCAN_ID = "scanId"
    const val SCANNER_TYPE = "scannerType"
    const val SCAN_DETAILS = "details"
    const val UPSTREAM = "upstream"
    const val CONTENT = "content"
    const val ERROR = "error"
}

internal object EventValues {

    const val CUSTOM_HOSTNAME = "custom_hostname"
    const val CUSTOM_HEADER = "custom_header"
    const val CUSTOM_DATA = "custom_data"
    const val FILES = "file"
    const val FIELDS = "textField"
}
