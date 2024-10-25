package com.ot.doggies.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}

class ApplicationDispatcherProvider : DispatcherProvider {
    override fun io() = Dispatchers.IO

    override fun ui() = Dispatchers.Main

    override fun default() = Dispatchers.Default
}