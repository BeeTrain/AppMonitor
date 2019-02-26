package ru.chernakov.appmonitor

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import ru.chernakov.appmonitor.di.AppComponent
import ru.chernakov.appmonitor.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().build()
    }
}