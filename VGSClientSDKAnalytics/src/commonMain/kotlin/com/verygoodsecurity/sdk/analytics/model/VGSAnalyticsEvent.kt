package com.verygoodsecurity.sdk.analytics.model

import com.verygoodsecurity.sdk.analytics.EventParams
import com.verygoodsecurity.sdk.analytics.EventTypes
import com.verygoodsecurity.sdk.analytics.EventValues
import com.verygoodsecurity.sdk.analytics.utils.currentTimeMillis
import kotlin.jvm.JvmName

sealed class VGSAnalyticsEvent {

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
        private val ui: String? = null,
    ) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.FIELD_ATTACH

        override val params: MutableMap<String, Any> = mutableMapOf<String, Any>(
            EventParams.FIELD_TYPE to fieldType
        ).apply {
            contentPath?.let { put(EventParams.CONTENT_PATH, contentPath) }
            ui?.let { put(EventParams.UI, ui) }
        }
    }

    data class FieldDetach(private val fieldType: String) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.FIELD_DETACH

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
        )
    }

    data class Cname(
        private val status: VGSAnalyticsStatus,
        private val hostname: String,
        private val latency: Long? = null
    ) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.CNAME

        override val params: MutableMap<String, Any> = mutableMapOf<String, Any>(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.HOSTNAME to hostname,
        ).apply {
            latency?.let { put(EventParams.LATENCY, latency) }
        }
    }

    class Request(
        status: VGSAnalyticsStatus,
        code: Int,
        content: List<String>,
        upstream: VGSAnalyticsUpstream,
    ) : VGSAnalyticsEvent() {

        constructor(
            status: VGSAnalyticsStatus,
            code: Int,
            content: List<String>,
        ) : this(
            status = status, code = code, content = content, upstream = VGSAnalyticsUpstream.CUSTOM
        )

        override val type: String = EventTypes.REQUEST

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.CODE to code,
            EventParams.CONTENT to content,
            EventParams.UPSTREAM to upstream.getAnalyticsName()
        )

        class Builder(
            private val status: VGSAnalyticsStatus,
            private val code: Int,
            private val upstream: VGSAnalyticsUpstream = VGSAnalyticsUpstream.CUSTOM
        ) {

            private val content: MutableList<String> = mutableListOf()

            fun mappingPolicy(policy: VGSAnalyticsMappingPolicy) = this.also {
                content.add(policy.analyticsValue)
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
                status = status, code = code, upstream = upstream, content = content
            )
        }
    }

    data class Response(
        private val status: VGSAnalyticsStatus,
        private val code: Int,
        private val upstream: VGSAnalyticsUpstream,
        private val errorMessage: String?,
    ) : VGSAnalyticsEvent() {

        constructor(
            status: VGSAnalyticsStatus,
            code: Int,
            errorMessage: String?,
        ) : this(status = status, code = code, upstream = VGSAnalyticsUpstream.CUSTOM, errorMessage)

        override val type: String = EventTypes.RESPONSE

        override val params: MutableMap<String, Any> = mutableMapOf<String, Any>(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.CODE to code,
            EventParams.UPSTREAM to upstream.getAnalyticsName(),
        ).apply {
            errorMessage?.let { put(EventParams.ERROR, errorMessage) }
        }
    }

    data class Autofill(private val fieldType: String) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.AUTOFILL

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
        )
    }

    data class AttachFile(private val status: VGSAnalyticsStatus) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.ATTACH_FILE

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.getAnalyticsName(),
        )
    }

    data class Scan(
        val status: VGSAnalyticsStatus,
        val scannerType: VGSAnalyticsScannerType,
        val scanId: String? = null,
        val scanDetails: String? = null,
        val errorCode: Int? = null
    ) : VGSAnalyticsEvent() {

        constructor(
            status: VGSAnalyticsStatus,
            scannerType: VGSAnalyticsScannerType,
        ) : this(
            status = status,
            scannerType = scannerType,
            scanId = null,
            scanDetails = null,
            errorCode = null
        )

        constructor(
            status: VGSAnalyticsStatus,
            scannerType: VGSAnalyticsScannerType,
            errorCode: Int
        ) : this(
            status = status,
            scannerType = scannerType,
            scanId = null,
            scanDetails = null,
            errorCode = errorCode
        )

        override val type: String = EventTypes.SCAN

        override val params: MutableMap<String, Any> = mutableMapOf<String, Any>(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.SCANNER_TYPE to scannerType.analyticsValue,
        ).apply {
            scanId?.let { put(EventParams.SCAN_ID, it) }
            scanDetails?.let { put(EventParams.SCAN_DETAILS, scanDetails) }
            errorCode?.let { put(EventParams.ERROR_CODE, it) }
        }
    }

    data class CopyToClipboard(
        val fieldType: String, val contentPath: String, val format: VGSAnalyticsCopyFormat
    ) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.COPY_TO_CLIPBOARD

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
            EventParams.CONTENT_PATH to contentPath,
            EventParams.COPY_FORMAT to format.name.lowercase(),
        )
    }

    data class SecureTextRange(
        val fieldType: String,
        val contentPath: String,
    ) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.SET_SECURE_TEXT_RANGE

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.FIELD_TYPE to fieldType,
            EventParams.CONTENT_PATH to contentPath,
        )
    }

    data class ContentRendering(
        val status: VGSAnalyticsStatus,
        val fieldType: String,
        val contentPath: String,
    ) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.CONTENT_RENDERING

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.STATUS to status.getAnalyticsName(),
            EventParams.FIELD_TYPE to fieldType,
            EventParams.CONTENT_PATH to contentPath
        )
    }

    data class ContentSharing(val contentPath: String) : VGSAnalyticsEvent() {

        override val type: String = EventTypes.CONTENT_SHARING

        override val params: MutableMap<String, Any> = mutableMapOf(
            EventParams.CONTENT_PATH to contentPath
        )
    }
}