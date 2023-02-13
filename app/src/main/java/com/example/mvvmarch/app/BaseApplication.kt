package com.example.mvvmarch.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        // start logger debug
        // initLogger(BuildConfig.DEBUG)
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
    }
}
