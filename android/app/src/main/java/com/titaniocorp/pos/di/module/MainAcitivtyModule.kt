package com.titaniocorp.pos.di.module

import com.titaniocorp.pos.app.ui.MainActivity
import com.titaniocorp.pos.app.ui.base.activity.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
    abstract fun contributeBaseActivity(): BaseActivity
}