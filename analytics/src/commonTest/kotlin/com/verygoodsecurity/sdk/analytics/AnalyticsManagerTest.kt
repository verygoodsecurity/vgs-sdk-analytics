package com.verygoodsecurity.sdk.analytics

import com.verygoodsecurity.sdk.analytics.data.repository.AnalyticsRepository
import com.verygoodsecurity.sdk.analytics.model.Event
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.test.BeforeTest
import kotlin.test.Test

class AnalyticsManagerTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val repository: AnalyticsRepository = mock<AnalyticsRepository>()

    private val provider: FakeProvider = FakeProvider(TestScope(dispatcher), dispatcher, repository)

    private val analyticsManager = AnalyticsManager(
        vault = "testVault",
        environment = "testEnvironment",
        source = "testSource",
        sourceVersion = "testSourceVersion",
        provider = provider
    )

    @BeforeTest
    fun setup() {
        everySuspend { repository.capture(any()) } returns Unit
    }

    @Test
    fun capture_calledOnce() {
        // Arrange
        val event = Event.FieldAttach(fieldType = "testFieldType", contentPath = "testContentPath")

        // Act
        analyticsManager.capture(event)

        // Assert
        verifySuspend(exactly(1)) { repository.capture(any()) }
    }

    internal class FakeProvider(
        private val testScope: CoroutineScope,
        private val testDispatcher: CoroutineDispatcher,
        private val repository: AnalyticsRepository
    ) : Provider() {

        override val scope: CoroutineScope
            get() = testScope

        override val dispatcher: CoroutineDispatcher
            get() = testDispatcher

        override fun getAnalyticsRepository(): AnalyticsRepository = repository

        override fun getDefaultEventParams(
            vault: String,
            environment: String,
            source: String,
            sourceVersion: String
        ): Map<String, Any> = emptyMap()
    }
}