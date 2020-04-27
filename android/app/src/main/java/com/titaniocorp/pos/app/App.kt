package com.titaniocorp.pos.app

import android.app.Application
import com.titaniocorp.pos.BuildConfig
import com.titaniocorp.pos.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Singleton de aplicación de toda la APP.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class App: Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            //Stetho.initializeWithDefaults(this)
        }
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    companion object {
        lateinit var instance: App
            private set
    }
}