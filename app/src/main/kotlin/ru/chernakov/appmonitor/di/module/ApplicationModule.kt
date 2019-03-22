package ru.chernakov.appmonitor.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.UIThread
import ru.chernakov.appmonitor.data.executor.JobExecutor
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.data.repository.EventRepository
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.domain.interactor.LoadHistory
import ru.chernakov.appmonitor.domain.interactor.ScanPackages
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

    @Provides
    @Singleton
    internal fun provideLoadApplications(
        applicationRepository: ApplicationRepository,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ): LoadApplications {
        return LoadApplications(applicationRepository, threadExecutor, postExecutionThread)
    }

    @Provides
    @Singleton
    internal fun provideLoadHistory(
        eventRepository: EventRepository,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ): LoadHistory {
        return LoadHistory(eventRepository, threadExecutor, postExecutionThread)
    }

    @Provides
    @Singleton
    internal fun provideScanPackages(
        applicationRepository: ApplicationRepository,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ): ScanPackages {
        return ScanPackages(applicationRepository, threadExecutor, postExecutionThread)
    }

}