package com.ot.doggies

import com.ot.doggies.di.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.rules.TestWatcher

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
) : TestWatcher(), org.junit.jupiter.api.extension.BeforeAllCallback,
   AfterAllCallback {
    val testDispatcherProvider = object : DispatcherProvider {
        override fun io(): CoroutineDispatcher = dispatcher
        override fun ui(): CoroutineDispatcher = dispatcher
        override fun default(): CoroutineDispatcher = dispatcher
    }

    val testScope: TestScope = TestScope(dispatcher)

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
        dispatcher.scheduler.cancel()
    }
}