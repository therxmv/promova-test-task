package com.therxmv.promovatest

import android.app.Application
import com.therxmv.base.network.networkModule
import com.therxmv.featuremovies.data.moviesDataModule
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

            modules(networkModule, moviesDataModule)
        }
    }
}