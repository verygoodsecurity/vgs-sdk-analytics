package com.verygoodsecurity.sdk.analytics.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UpstreamTest {

    @Test
    fun getAnalyticsName_upstreamTokenization_correctReturned() {
        // Arrange
        val upstream = VGSAnalyticsUpstream.TOKENIZATION

        // Act
        val analyticsName = upstream.getAnalyticsName()

        // Assert
        assertEquals("tokenization", analyticsName)
    }

    @Test
    fun getAnalyticsName_upstreamCustom_correctReturned() {
        // Arrange
        val upstream = VGSAnalyticsUpstream.CUSTOM

        // Act
        val analyticsName = upstream.getAnalyticsName()

        // Assert
        assertEquals("custom", analyticsName)
    }

    @Test
    fun getAnalyticsName_upstreamCmp_correctReturned() {
        // Arrange
        val upstream = VGSAnalyticsUpstream.CMP

        // Act
        val analyticsName = upstream.getAnalyticsName()

        // Assert
        assertEquals("cmp", analyticsName)
    }
}