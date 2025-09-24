package com.spruhs

import android.app.Application
import com.spruhs.auth.di.authModule
import com.spruhs.di.platformModule
import com.spruhs.group.di.groupModule
import com.spruhs.main.di.mainModule
import com.spruhs.match.di.matchModule
import com.spruhs.statistics.di.statisticModule
import com.spruhs.user.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KickItApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules =
            platformModule + authModule + userModule + mainModule + groupModule + matchModule +
                statisticModule
        startKoin {
            androidContext(applicationContext)
            modules(modules)
        }
    }
}