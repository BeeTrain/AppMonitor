package ru.chernakov.appmonitor

import android.app.Application
import ru.chernakov.appmonitor.di.component.AppComponent
import ru.chernakov.appmonitor.di.component.DaggerAppComponent
import ru.chernakov.appmonitor.di.module.ApplicationModule

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        instance = this

        component = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        component.inject(this)
    }

    fun getAppComponent(): AppComponent {
        return component
    }

    companion object {
        lateinit var instance: App private set
    }
}