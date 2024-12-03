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
    fun fieldInit_correctParamsReturned() {
        // Arrange
        val fieldType = "testType"
        val contentPath = "testPath"
        val event = Event.FieldInit(fieldType, contentPath)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.FIELD_INIT)
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
        assertEquals(params[EventParams.CONTENT_PATH], contentPath)
    }

    @Test
    fun cname_statusOk_correctParamsReturned() {
        // Arrange
        val status = Status.OK
        val hostname = "testHostname"
        val event = Event.Cname(status, hostname)

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
        val status = Status.FAILED
        val hostname = "testHostname"
        val event = Event.Cname(status, hostname)

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
        val status = Status.OK
        val code = 200
        val upstream = Upstream.get(false)

        val event = Event.Request.Builder(status, code, upstream)
            .customHostname()
            .customHeader()
            .customData()
            .fields()
            .files()
            .mappingPolicy(MappingPolicy.NESTED_JSON)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.STATUS_CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(EventValues.CUSTOM_HOSTNAME))
        assertTrue(content.contains(EventValues.CUSTOM_HEADER))
        assertTrue(content.contains(EventValues.CUSTOM_DATA))
        assertTrue(content.contains(EventValues.FIELDS))
        assertTrue(content.contains(EventValues.FILES))
        assertTrue(content.contains(MappingPolicy.NESTED_JSON.getAnalyticsName()))
    }

    @Test
    fun request_statusOk_nestedJsonWithArrayMerge_correctParamsReturned() {
        // Arrange
        val status = Status.OK
        val code = 200
        val upstream = Upstream.get(false)

        val event = Event.Request.Builder(status, code, upstream)
            .mappingPolicy(MappingPolicy.NESTED_JSON_ARRAYS_MERGE)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.STATUS_CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(MappingPolicy.NESTED_JSON_ARRAYS_MERGE.getAnalyticsName()))
    }

    @Test
    fun request_statusOk_nestedJsonWithArrayOverwrite_correctParamsReturned() {
        // Arrange
        val status = Status.OK
        val code = 200
        val upstream = Upstream.get(false)

        val event = Event.Request.Builder(status, code, upstream)
            .mappingPolicy(MappingPolicy.NESTED_JSON_ARRAYS_OVERWRITE)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.STATUS_CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(MappingPolicy.NESTED_JSON_ARRAYS_OVERWRITE.getAnalyticsName()))
    }

    @Test
    fun request_statusFailed_flatJson_correctParamsReturned() {
        // Arrange
        val status = Status.FAILED
        val code = 500
        val upstream = Upstream.get(false)

        val event = Event.Request.Builder(status, code, upstream)
            .mappingPolicy(MappingPolicy.FLAT_JSON)
            .build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.STATUS_CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.contains(MappingPolicy.FLAT_JSON.getAnalyticsName()))
    }

    @Test
    fun request_statusFailed_flatJson_emptyContent_tokenizationUpstream_correctParamsReturned() {
        // Arrange
        val status = Status.FAILED
        val code = 500
        val upstream = Upstream.get(true)

        val event = Event.Request.Builder(status, code, upstream).build()

        // Act
        val params = event.getParams()
        val content = params[EventParams.CONTENT] as List<*>

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.REQUEST)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.STATUS_CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertTrue(content.isEmpty())
    }

    @Test
    fun response_statusOk_tokenizationUpstream_correctParamsReturned() {
        // Arrange
        val status = Status.OK
        val code = 200
        val upstream = Upstream.get(true)

        val event = Event.Response(status, code, upstream)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.RESPONSE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.STATUS_CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertFalse(params.contains(EventParams.ERROR))
    }

    @Test
    fun response_statusFailed_customUpstream_correctParamsReturned() {
        // Arrange
        val status = Status.OK
        val code = 200
        val upstream = Upstream.get(false)
        val errorMessage = "Test error"

        val event = Event.Response(status, code, upstream, errorMessage)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.RESPONSE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.STATUS_CODE], code)
        assertEquals(params[EventParams.UPSTREAM], upstream.getAnalyticsName())
        assertEquals(params[EventParams.ERROR], errorMessage)
    }

    @Test
    fun autofill_correctParamsReturned() {
        // Arrange
        val fieldType = "testType"

        val event = Event.Autofill(fieldType)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.AUTOFILL)
        assertEquals(params[EventParams.FIELD_TYPE], fieldType)
    }

    @Test
    fun attachFile_statusOk_correctParamsReturned() {
        // Arrange
        val status = Status.OK

        val event = Event.AttachFile(status)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.ATTACH_FILE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
    }

    @Test
    fun attachFile_statusFailed_correctParamsReturned() {
        // Arrange
        val status = Status.FAILED

        val event = Event.AttachFile(status)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.ATTACH_FILE)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
    }

    @Test
    fun scan_statusOk_correctParamsReturned() {
        // Arrange
        val status = Status.OK
        val scanId = "testScanId"
        val scanDetails = "testScanDetails"
        val scannerType = "testScannerType"

        val event = Event.Scan(status, scanId, scanDetails, scannerType)

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
        val status = Status.FAILED
        val scanId = "testScanId"
        val scanDetails = "testScanDetails"
        val scannerType = "testScannerType"

        val event = Event.Scan(status, scanId, scanDetails, scannerType)

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
        val status = Status.CANCEL
        val scanId = "testScanId"
        val scanDetails = "testScanDetails"
        val scannerType = "testScannerType"

        val event = Event.Scan(status, scanId, scanDetails, scannerType)

        // Act
        val params = event.getParams()

        // Assert
        assertEquals(params[EventParams.TYPE], EventTypes.SCAN)
        assertEquals(params[EventParams.STATUS], status.getAnalyticsName())
        assertEquals(params[EventParams.SCAN_ID], scanId)
        assertEquals(params[EventParams.SCAN_DETAILS], scanDetails)
        assertEquals(params[EventParams.SCANNER_TYPE], scannerType)
    }
}