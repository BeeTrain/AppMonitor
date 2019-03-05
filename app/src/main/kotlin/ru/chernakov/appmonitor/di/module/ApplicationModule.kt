package ru.chernakov.appmonitor.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.UIThread
import ru.chernakov.appmonitor.data.executor.JobExecutor
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import javax.inject.Singleton

@Module
class ApplicationModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    internal fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor {
        return jobExecutor
    }

    @Provides
    @Singleton
    internal fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread {
        return uiThread
    }
}