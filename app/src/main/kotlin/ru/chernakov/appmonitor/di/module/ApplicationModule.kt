package ru.chernakov.appmonitor.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.di.scope.PerApplication
import javax.inject.Singleton

@Module
class ApplicationModule(private val app: App) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return app
    }
}