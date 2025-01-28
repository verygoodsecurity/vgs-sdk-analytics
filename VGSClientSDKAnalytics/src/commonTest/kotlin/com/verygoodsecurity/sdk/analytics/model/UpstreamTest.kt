package com.verygoodsecurity.sdk.analytics.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UpstreamTest {

    @Test
    fun get_true_tokenizationReturned() {
        // Arrange
        val isTokenization = true

        // Act
        val upstream = VGSAnalyticsUpstream.get(isTokenization)

        // Assert
        assertEquals(VGSAnalyticsUpstream.TOKENIZATION, upstream)
    }

    @Test
    fun get_false_customReturned() {
        // Arrange
        val isTokenization = false

        // Act
        val upstream = VGSAnalyticsUpstream.get(isTokenization)

        // Assert
        assertEquals(VGSAnalyticsUpstream.CUSTOM, upstream)
    }

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
}