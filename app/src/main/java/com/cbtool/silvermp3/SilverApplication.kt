package com.cbtool.silvermp3

import android.app.Application
import com.cbtool.silvermp3.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SilverApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SilverApplication)
            modules(appModule)
        }
    }


}