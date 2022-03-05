package com.tdh.windydemo

import android.app.Application

class App : Application() {
    companion object {
        lateinit var newInstance: App
    }

    override fun onCreate() {
        super.onCreate()
        newInstance = this
    }
}