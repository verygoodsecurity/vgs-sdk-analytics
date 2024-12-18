package com.verygoodsecurity.sdk.analytics.model

import com.verygoodsecurity.sdk.analytics.EventParams
import com.verygoodsecurity.sdk.analytics.EventTypes
import com.verygoodsecurity.sdk.analytics.EventValues
import com.verygoodsecurity.sdk.analytics.utils.currentTimeMillis
import kotlin.jvm.JvmName

sealed class Event {

    protected abstract val type: String

    protected abstract val params: MutableMap<String, Any>

    private val timestamp: Long = currentTimeMillis()

    @JvmName("getEventParams")
    fun getParams(): Map<String, Any> {
        return params.also {
            it[EventParams.TYPE] = type
            it[EventParams.TIMESTAMP] = timestamp
        }
    }

    data class FieldAttach(
        private val fieldType: String,
        private val contentPath: String? = null,
        private val ui: String? = null, // TODO: Use expected/actual for enum?
    ) : Event() {

        override val type: String = EventTypes.FIELD_ATTACH

        override val params: MutableMap<String, Any> = mutableMapOf<String, Any>(
            EventParams.FIELD_TYPE to fieldType
        ).apply {
            contentPath?.let { put(EventParams.CONTENT_PATH, contentPath) }
            ui?.let { put(EventParams.UI, ui) }
        }
    }

    data class FieldDetach(private val fieldType: String) : Event() {

        override val type: String = EventTypes.FIELD_DETACH

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
        )
    }

    data class Cname(
        private val status: Status,
        private val hostname: String,
        private val latency: Long? = null
    ) : Event() {

        override val type: String = EventTypes.CNAME

        override val params: MutableMap<String, Any> = mutableMapOf<String, Any>(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.HOSTNAME to hostname,
        ).apply {
            latency?.let { put(EventParams.LATENCY, latency) }
        }
    }

    class Request private constructor(
        status: Status,
        code: Int,
        content: List<String>,
        upstream: Upstream,
    ) : Event() {

        override val type: String = EventTypes.REQUEST

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.STATUS_CODE to code,
            EventParams.CONTENT to content,
            EventParams.UPSTREAM to upstream.getAnalyticsName()
        )

        class Builder(
            private val status: Status,
            private val code: Int,
            private val upstream: Upstream = Upstream.CUSTOM
        ) {

            private val content: MutableList<String> = mutableListOf()

            fun mappingPolicy(policy: MappingPolicy) = this.also {
                content.add(policy.getAnalyticsName())
            }

            fun customHostname() = this.also {
                content.add(EventValues.CUSTOM_HOSTNAME)
            }

            fun customHeader() = this.also {
                content.add(EventValues.CUSTOM_HEADER)
            }

            fun customData() = this.also {
                content.add(EventValues.CUSTOM_DATA)
            }

            fun fields() = this.also {
                content.add(EventValues.FIELDS)
            }

            fun files() = this.also {
                content.add(EventValues.FILES)
            }

            fun pdf() = this.also {
                content.add(EventValues.PDF)
            }

            fun build() = Request(
                status = status,
                code = code,
                upstream = upstream,
                content = content
            )
        }
    }

    data class Response(
        private val status: Status,
        private val code: Int,
        private val upstream: Upstream = Upstream.CUSTOM,
        private val errorMessage: String? = null,
    ) : Event() {

        override val type: String = EventTypes.RESPONSE

        override val params: MutableMap<String, Any> = mutableMapOf<String, Any>(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.STATUS_CODE to code,
            EventParams.UPSTREAM to  upstream.getAnalyticsName(),
        ).apply {
            errorMessage?.let { put(EventParams.ERROR, errorMessage) }
        }
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
            EventParams.STATUS to status.getAnalyticsName(),
        )
    }

    data class Scan(
        val status: Status,
        val scanId: String,
        val scanDetails: String,
        val scannerType: String,
    ) : Event() {

        override val type: String = EventTypes.SCAN

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.SCAN_ID to scanId,
            EventParams.SCAN_DETAILS to scanDetails,
            EventParams.SCANNER_TYPE to scannerType,
        )
    }

    data class CopyToClipboard(
        val fieldType: String,
        val format: CopyFormat,
        val triggerAction: String = EventValues.COPY_TO_CLIPBOARD_ACTION_CLICK,
    ) : Event() {

        override val type: String = EventTypes.COPY_TO_CLIPBOARD

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
            EventParams.COPY_FORMAT to format.name.lowercase(),
            EventParams.STATUS to triggerAction, // TODO: Rename param?
        )
    }

    data class SecureTextRange(
        val fieldType: String,
        val contentPath: String,
    ) : Event() {

        override val type: String = EventTypes.SET_SECURE_TEXT_RANGE

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
            EventParams.CONTENT_PATH to contentPath,
        )
    }

    data class ContentRendering(
        val fieldType: String,
        val status: Status,
    ) : Event() {

        override val type: String = EventTypes.CONTENT_RENDERING

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
            EventParams.STATUS to status.getAnalyticsName(),
        )
    }

    data class ContentSharing(val fieldType: String) : Event() {

        override val type: String = EventTypes.CONTENT_SHARING

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
        )
    }
}