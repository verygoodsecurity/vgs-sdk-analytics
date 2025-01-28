package com.verygoodsecurity.sdk.analytics.model

import kotlin.test.Test
import kotlin.test.assertEquals

class StatusTest {

    @Test
    fun getAnalyticsName_statusOk_correctReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.OK

        // Act
        val analyticsName = status.getAnalyticsName()

        // Assert
        assertEquals("Ok", analyticsName)
    }

    @Test
    fun getAnalyticsName_statusFailed_correctReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.FAILED

        // Act
        val analyticsName = status.getAnalyticsName()

        // Assert
        assertEquals("Failed", analyticsName)
    }

    @Test
    fun getAnalyticsName_statusCancel_correctReturned() {
        // Arrange
        val status = VGSAnalyticsStatus.CANCELED

        // Act
        val analyticsName = status.getAnalyticsName()

        // Assert
        assertEquals("Canceled", analyticsName)
    }
}