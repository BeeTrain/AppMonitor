package ru.chernakov.appmonitor

import android.app.Application
import android.content.Intent
import android.os.Build
import ru.chernakov.appmonitor.data.AppPreferences
import ru.chernakov.appmonitor.di.component.AppComponent
import ru.chernakov.appmonitor.di.component.DaggerAppComponent
import ru.chernakov.appmonitor.di.module.ApplicationModule
import ru.chernakov.appmonitor.presentation.service.PackageService


class App : Application() {

    private lateinit var component: AppComponent

    companion object {
        lateinit var instance: App private set

        lateinit var appPreferences: AppPreferences

        var isServiceRunning: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        appPreferences = AppPreferences(this)

        component = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        startPackageService()
    }

    fun getAppComponent(): AppComponent {
        return component
    }

    private fun startPackageService() {
        val packageService = Intent(this, PackageService::class.java)
        packageService.action = PackageService.ACTION_START
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(packageService)
        } else {
            startService(packageService)
        }
    }
}