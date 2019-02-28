package ru.chernakov.appmonitor.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.di.scope.PerApplication

@Module
class ApplicationModule(private val app: App) {

    @Provides
    @PerApplication
    fun provideApplication(): Application {
        return app
    }
}