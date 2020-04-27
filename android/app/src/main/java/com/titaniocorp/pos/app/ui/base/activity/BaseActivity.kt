package com.titaniocorp.pos.app.ui.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.titaniocorp.pos.util.Configurations
import com.titaniocorp.pos.util.Constants
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber

/**
 * Actividad base de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!Configurations.init(this)){
            Timber.tag(Constants.TAG_APP_DEBUG).e("Configurations no ha podido ser leido.")
        }
    }
}

