package com.spruhs

import android.app.Application
import com.spruhs.auth.di.authModule
import com.spruhs.di.platformModule
import com.spruhs.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KickItApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules = viewModelsModule + authModule + platformModule
        startKoin {
            androidContext(applicationContext)
            modules(modules)
        }
    }
}