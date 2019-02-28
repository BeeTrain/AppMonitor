package ru.chernakov.appmonitor

import android.app.Application
import ru.chernakov.appmonitor.data.AppPreferences
import ru.chernakov.appmonitor.di.component.AppComponent
import ru.chernakov.appmonitor.di.component.DaggerAppComponent
import ru.chernakov.appmonitor.di.module.ApplicationModule


class App : Application() {

    private lateinit var component: AppComponent

    companion object {
        lateinit var instance: App private set

        lateinit var appPreferences: AppPreferences
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        appPreferences = AppPreferences(this)

        component = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    fun getAppComponent(): AppComponent {
        return component
    }

}