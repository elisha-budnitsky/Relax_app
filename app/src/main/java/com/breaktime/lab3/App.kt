package com.breaktime.lab3

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.breaktime.lab3.di.appModule
import com.breaktime.lab3.service.AccelerometerService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule))
        }
        startService(Intent(this, AccelerometerService::class.java))
    }
}