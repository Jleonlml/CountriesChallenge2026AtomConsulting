package com.devart.countrieschallange2026

import android.app.Application
import com.devart.countrieschallange2026.di.AppComponent
import com.devart.countrieschallange2026.di.DaggerAppComponent

class DaggerApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
    }
}