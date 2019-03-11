package ru.chernakov.appmonitor.di.component

import android.app.Application
import dagger.Component
import ru.chernakov.appmonitor.di.module.ApplicationModule
import ru.chernakov.appmonitor.di.module.GlobalNavigationModule
import ru.chernakov.appmonitor.domain.executor.PostExecutionThread
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import ru.chernakov.appmonitor.presentation.service.PackageService
import ru.chernakov.appmonitor.presentation.ui.AppActivity
import ru.chernakov.appmonitor.presentation.ui.info.InfoFragment
import ru.chernakov.appmonitor.presentation.ui.list.ListFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        ApplicationModule::class,
        GlobalNavigationModule::class
    )
)
interface AppComponent {

    fun inject(packageService: PackageService)

    fun inject(appActivity: AppActivity)

    fun inject(infoFragment: InfoFragment)

    fun inject(listFragment: ListFragment)


    fun application(): Application

    fun threadExecutor(): ThreadExecutor
    fun postExecutionThread(): PostExecutionThread
}