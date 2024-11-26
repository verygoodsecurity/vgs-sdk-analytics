package com.verygoodsecurity.sdk.analytics.model

import com.verygoodsecurity.sdk.analytics.EventParams
import com.verygoodsecurity.sdk.analytics.EventTypes
import com.verygoodsecurity.sdk.analytics.Http
import com.verygoodsecurity.sdk.analytics.utils.currentTimeMillis
import kotlin.jvm.JvmName

sealed class Event {

    protected abstract val type: String

    abstract val params: MutableMap<String, Any>

    private val timestamp: Long = currentTimeMillis()

    @JvmName("getEventParams")
    fun getParams(): Map<String, Any> {
        return params.also {
            it[EventParams.TYPE] = type
            it[EventParams.TIMESTAMP] = timestamp
        }
    }

    data class FieldInit(
        private val fieldType: String,
        private val contentPath: String,
    ) : Event() {

        override val type: String = EventTypes.FIELD_INIT

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
            EventParams.CONTENT_PATH to contentPath
        )
    }

    data class Cname(
        private val status: Status,
        private val hostname: String,
    ) : Event() {

        override val type: String = EventTypes.AUTOFILL

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.capitalize(),
            EventParams.HOSTNAME to hostname,
        )
    }

    // TODO: Complete
    data class Request(
        private val status: Status,
        private val code: Int = Http.SUCCESS_STATUS_CODE
    ) : Event() {

        override val type: String = EventTypes.REQUEST

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.capitalize(),
            EventParams.STATUS_CODE to code
        )
    }

    // TODO: Complete
    data class Response(
        private val status: Status,
        private val code: Int = Http.SUCCESS_STATUS_CODE
    ) : Event() {

        override val type: String = EventTypes.RESPONSE

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.capitalize(),
            EventParams.STATUS_CODE to code
        )
    }

    data class Autofill(private val fieldType: String) : Event() {

        override val type: String = EventTypes.AUTOFILL

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
        )
    }

    data class AttachFile(private val status: Status) : Event() {

        override val type: String = EventTypes.ATTACH_FILE

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.capitalize(),
        )
    }

    data class Scan(
        val status: String,
        val scanId: String,
        val scanDetails: String,
        val scannerType: String,
    ) : Event() {

        override val type: String = EventTypes.SCAN

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status,
            EventParams.SCAN_ID to scanId,
            EventParams.SCAN_DETAILS to scanDetails,
            EventParams.SCANNER_TYPE to scannerType,
        )
    }
}