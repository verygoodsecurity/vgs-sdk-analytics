package com.verygoodsecurity.sdk.analytics.model

import com.verygoodsecurity.sdk.analytics.EventParams
import com.verygoodsecurity.sdk.analytics.EventTypes
import com.verygoodsecurity.sdk.analytics.EventValues
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// TODO: Mock timestamp for time params
class EventTest {

    @Test
    fun fieldAttach_correctParamsReturned() {
        // Arrange
        val fieldType = "testType"
        val contentPath = "testPath"
        val event = VGSAnalyticsEvent.FieldAttach(fieldType, contentPath)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.FIELD_ATTACH)
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
        assertEquals(params[EventParams.CONTENT_PATH], contentPath)
    }

    @Test
    fun fieldDetach_correctParamsReturned() {
        // Arrange
        val fieldType = "testType"
        val event = VGSAnalyticsEvent.FieldDetach(fieldType)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.FIELD_DETACH)
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
    }

    @Test
    fun cname_statusOk_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val hostname = "testHostname"
        val event = VGSAnalyticsEvent.Cname(status, hostname)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.CNAME)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.HOSTNAME], hostname)
    }

    @Test
    fun cname_statusFailed_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.FAILED
        val hostname = "testHostname"
        val event = VGSAnalyticsEvent.Cname(status, hostname)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.CNAME)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.HOSTNAME], hostname)
    }

    @Test
    fun request_statusOk_nestedJson_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val code = 200
        val upstream = VGSAnalyticsUpstream.get(false)

        val event = VGSAnalyticsEvent.Request.Builder(status, code, upstream)
            .customHostname()
            .customHeader()
            .customData()
            .fields()
            .files()
            .mappingPolicy(VGSAnalyticsMappingPolicy.NESTED_JSON)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(EventValues.CUSTOM_HOSTNAME))
        assertTrue(content.contains(EventValues.CUSTOM_HEADER))
        assertTrue(content.contains(EventValues.CUSTOM_DATA))
        assertTrue(content.contains(EventValues.FIELDS))
        assertTrue(content.contains(EventValues.FILES))
        assertTrue(content.contains(VGSAnalyticsMappingPolicy.NESTED_JSON.analyticsValue))
    }

    @Test
    fun request_statusOk_nestedJsonWithArrayMerge_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val code = 200
        val upstream = VGSAnalyticsUpstream.get(false)

        val event = VGSAnalyticsEvent.Request.Builder(status, code, upstream)
            .mappingPolicy(VGSAnalyticsMappingPolicy.NESTED_JSON_ARRAYS_MERGE)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(VGSAnalyticsMappingPolicy.NESTED_JSON_ARRAYS_MERGE.analyticsValue))
    }

    @Test
    fun request_statusOk_nestedJsonWithArrayOverwrite_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val code = 200
        val upstream = VGSAnalyticsUpstream.get(false)

        val event = VGSAnalyticsEvent.Request.Builder(status, code, upstream)
            .mappingPolicy(VGSAnalyticsMappingPolicy.NESTED_JSON_ARRAYS_OVERWRITE)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(VGSAnalyticsMappingPolicy.NESTED_JSON_ARRAYS_OVERWRITE.analyticsValue))
    }

    @Test
    fun request_statusFailed_flatJson_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.FAILED
        val code = 500
        val upstream = VGSAnalyticsUpstream.get(false)

        val event = VGSAnalyticsEvent.Request.Builder(status, code, upstream)
            .mappingPolicy(VGSAnalyticsMappingPolicy.FLAT_JSON)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(VGSAnalyticsMappingPolicy.FLAT_JSON.analyticsValue))
    }

    @Test
    fun request_statusFailed_flatJson_emptyContent_tokenizationUpstream_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.FAILED
        val code = 500
        val upstream = VGSAnalyticsUpstream.get(true)

        val event = VGSAnalyticsEvent.Request.Builder(status, code, upstream).build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.isEmpty())
    }

    @Test
    fun response_statusOk_tokenizationUpstream_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val code = 200

        val event = VGSAnalyticsEvent.Response(status, code, errorMessage = null)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.RESPONSE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.CODE], code)
        assertFalse(params.contains(EventParams.ERROR))
    }

    @Test
    fun response_statusFailed_customUpstream_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val code = 200
        val upstream = VGSAnalyticsUpstream.TOKENIZATION
        val errorMessage = "Test error"

        val event = VGSAnalyticsEvent.Response(status, code, upstream, errorMessage)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.RESPONSE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertEquals(params[EventParams.ERROR], errorMessage)
    }

    @Test
    fun autofill_correctParamsReturned() {
        // Arrange
        val fieldType = "testType"

        val event = VGSAnalyticsEvent.Autofill(fieldType)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.AUTOFILL)
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
    }

    @Test
    fun attachFile_statusOk_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK

        val event = VGSAnalyticsEvent.AttachFile(status)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.ATTACH_FILE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
    }

    @Test
    fun attachFile_statusFailed_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.FAILED

        val event = VGSAnalyticsEvent.AttachFile(status)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.ATTACH_FILE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
    }

    @Test
    fun scan_statusOk_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val scanId = "testScanId"
        val scanDetails = "testScanDetails"
        val scannerType = "testScannerType"

        val event = VGSAnalyticsEvent.Scan(status, scanId, scanDetails, scannerType)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.SCAN)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.SCAN_ID], scanId)
        assertEquals(params[EventParams.SCAN_DETAILS], scanDetails)
        assertEquals(params[EventParams.SCANNER_TYPE], scannerType)
    }

    @Test
    fun scan_statusFailed_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.FAILED
        val scanId = "testScanId"
        val scanDetails = "testScanDetails"
        val scannerType = "testScannerType"

        val event = VGSAnalyticsEvent.Scan(status, scanId, scanDetails, scannerType)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.SCAN)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.SCAN_ID], scanId)
        assertEquals(params[EventParams.SCAN_DETAILS], scanDetails)
        assertEquals(params[EventParams.SCANNER_TYPE], scannerType)
    }

    @Test
    fun scan_statusCancel_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.CANCELED
        val scanId = "testScanId"
        val scanDetails = "testScanDetails"
        val scannerType = "testScannerType"

        val event = VGSAnalyticsEvent.Scan(status, scanId, scanDetails, scannerType)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.SCAN)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.SCAN_ID], scanId)
        assertEquals(params[EventParams.SCAN_DETAILS], scanDetails)
        assertEquals(params[EventParams.SCANNER_TYPE], scannerType)
    }

    @Test
    fun copyToClipboard_raw_correctParamsReturned() {
        // Arrange
        val fieldType = "testType"
        val contentPath = "testContentPath"
        val format = VGSAnalyticsCopyFormat.RAW
        val event = VGSAnalyticsEvent.CopyToClipboard(fieldType, contentPath, format)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.COPY_TO_CLIPBOARD)
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
        assertEquals(params[EventParams.CONTENT_PATH], contentPath)
        assertEquals(params[EventParams.COPY_FORMAT], format.getAnalyticsName())
    }

    @Test
    fun secureTextRange_correctParamsReturned() {
        // Arrange
        val fieldType = "testType"
        val contentPath = "testContentPath"
        val event = VGSAnalyticsEvent.SecureTextRange(fieldType, contentPath)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.SET_SECURE_TEXT_RANGE)
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
        assertEquals(params[EventParams.CONTENT_PATH], contentPath)
    }

    @Test
    fun contentRendering_statusOk_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK
        val fieldType = "testType"
        val contentPath = "testContentPath"
        val event = VGSAnalyticsEvent.ContentRendering(status, fieldType, contentPath)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.CONTENT_RENDERING)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
        assertEquals(params[EventParams.CONTENT_PATH], contentPath)
    }

    @Test
    fun contentRendering_statusFailed_correctParamsReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.FAILED
        val fieldType = "testType"
        val contentPath = "testContentPath"
        val event = VGSAnalyticsEvent.ContentRendering(status, fieldType, contentPath)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.CONTENT_RENDERING)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
        assertEquals(params[EventParams.CONTENT_PATH], contentPath)
    }

    @Test
    fun contentSharing_correctParamsReturned() {
        // Arrange
        val contentPath = "testContentPath"
        val event = VGSAnalyticsEvent.ContentSharing(contentPath)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.CONTENT_SHARING)
        assertEquals(params[EventParams.CONTENT_PATH], contentPath)
    }
}