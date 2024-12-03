package com.verygoodsecurity.sdk.analytics

import com.verygoodsecurity.sdk.analytics.data.repository.AnalyticsRepository
import com.verygoodsecurity.sdk.analytics.model.Event
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AnalyticsManagerTest {

    private val dispatcher: TestDispatcher = StandardTestDispatcher()

    private val scope: CoroutineScope = TestScope(dispatcher)

    private val repository: AnalyticsRepository = mock { }

    private val provider: FakeProvider = FakeProvider(scope, repository)

    private val analyticsManager = AnalyticsManager(
        vault = "testVault",
        environment = "testEnvironment",
        source = "testSource",
        sourceVersion = "testSourceVersion",
        provider = provider
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun capture_calledOnce() {
        // Arrange
        val event = Event.FieldInit(fieldType = "testFieldType", contentPath = "testContentPath")

        // Act
        analyticsManager.capture(event)

        // Assert
        verifySuspend(exactly(1)) { repository.capture(any()) }
    }

    internal class FakeProvider(
        private val testScope: CoroutineScope,
        private val repository: AnalyticsRepository
    ) : Provider() {

        override val scope: CoroutineScope
            get() = testScope

        override fun getAnalyticsRepository(): AnalyticsRepository = repository

        override fun getDefaultEventParams(
            vault: String,
            environment: String,
            source: String,
            sourceVersion: String
        ): Map<String, Any> = emptyMap()
    }
}