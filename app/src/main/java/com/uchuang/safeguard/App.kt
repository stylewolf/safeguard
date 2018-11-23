package com.uchuang.safeguard

import android.app.Application
import android.util.Log
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SDKInitializer.initialize(applicationContext)
    }
}