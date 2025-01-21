package com.verygoodsecurity.sdk.analytics

import com.verygoodsecurity.sdk.analytics.data.repository.AnalyticsRepository
import com.verygoodsecurity.sdk.analytics.model.VGSAnalyticsEvent
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

    private val analyticsManager = VGSSharedAnalyticsManager(
        source = "testSource",
        sourceVersion = "testSourceVersion",
        dependencyManager = "testDependencyManager",
        provider = provider
    )

    private val vault = "testVault"

    private val environment = "testEnvironment"

    private val formId = "testFormId"

    @BeforeTest
    fun setup() {
        everySuspend { repository.capture(any()) } returns Unit
    }

    @Test
    fun capture_calledOnce() {
        // Arrange
        val event = VGSAnalyticsEvent.FieldAttach(
            fieldType = "testFieldType",
            contentPath = "testContentPath"
        )

        // Act
        analyticsManager.capture(vault, environment, formId, event)

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
            source: String,
            sourceVersion: String,
            dependencyManager: String
        ): Map<String, Any> = emptyMap()
    }
}