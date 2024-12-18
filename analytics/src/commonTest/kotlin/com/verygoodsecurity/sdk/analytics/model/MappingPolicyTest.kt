package com.verygoodsecurity.sdk.analytics.model

import kotlin.test.Test
import kotlin.test.assertEquals

class MappingPolicyTest {

    @Test
    fun getAnalyticsName_nestedJson_correctReturned() {
        // Arrange
        val policy = MappingPolicy.NESTED_JSON

        // Act
        val analyticsName = policy.analyticsValue

        // Assert
        assertEquals("nestedJson", analyticsName)
    }

    @Test
    fun getAnalyticsName_flatJson_correctReturned() {
        // Arrange
        val policy = MappingPolicy.FLAT_JSON

        // Act
        val analyticsName = policy.analyticsValue

        // Assert
        assertEquals("flatJson", analyticsName)
    }

    @Test
    fun getAnalyticsName_nestedJsonArraysMerge_correctReturned() {
        // Arrange
        val policy = MappingPolicy.NESTED_JSON_ARRAYS_MERGE

        // Act
        val analyticsName = policy.analyticsValue

        // Assert
        assertEquals("nestedJsonArrayMerge", analyticsName)
    }

    @Test
    fun getAnalyticsName_nestedJsonArraysOverwrite_correctReturned() {
        // Arrange
        val policy = MappingPolicy.NESTED_JSON_ARRAYS_OVERWRITE

        // Act
        val analyticsName = policy.analyticsValue

        // Assert
        assertEquals("nestedJsonArrayOverwrite", analyticsName)
    }
}