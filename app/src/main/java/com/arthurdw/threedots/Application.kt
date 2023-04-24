package com.arthurdw.threedots

import android.app.Application
import com.arthurdw.threedots.data.AppContainer
import com.arthurdw.threedots.data.DefaultAppContainer

class Application: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}