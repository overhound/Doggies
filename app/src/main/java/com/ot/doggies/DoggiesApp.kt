package com.ot.doggies

import android.app.Application
import com.ot.doggies.di.doggiesApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup.onKoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level

@OptIn(KoinExperimentalAPI::class)
class DoggiesApp: Application(){

    init {
        onKoinStartup {
            androidLogger(Level.ERROR)
            allowOverride(true)
            androidContext(this@DoggiesApp)
            androidFileProperties()
            modules(doggiesApp)
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}