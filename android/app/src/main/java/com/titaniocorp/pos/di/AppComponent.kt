package com.titaniocorp.pos.di

import android.app.Application
import com.titaniocorp.pos.app.App
import com.titaniocorp.pos.di.module.AppModule
import com.titaniocorp.pos.di.module.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Componente principal de la inyeccion de dependencias, acá se deben establecer todos los módulos
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: App)
}