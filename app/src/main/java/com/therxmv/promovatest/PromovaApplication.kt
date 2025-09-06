package com.therxmv.promovatest

import android.app.Application
import com.therxmv.promovatest.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PromovaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@PromovaApplication)
            modules(appModules)
        }
    }
}