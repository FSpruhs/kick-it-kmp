package com.spruhs

import android.app.Application
import com.spruhs.auth.di.authModule
import com.spruhs.di.platformModule
import com.spruhs.di.viewModelsModule
import com.spruhs.user.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KickItApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules = viewModelsModule + platformModule + authModule + userModule
        startKoin {
            androidContext(applicationContext)
            modules(modules)
        }
    }
}