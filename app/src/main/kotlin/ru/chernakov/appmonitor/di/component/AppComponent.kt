package ru.chernakov.appmonitor.di.component

import dagger.Component
import ru.chernakov.appmonitor.di.module.ApplicationModule
import ru.chernakov.appmonitor.di.module.GlobalNavigationModule
import ru.chernakov.appmonitor.di.module.LocalNavigationModule
import ru.chernakov.appmonitor.presentation.AppActivity
import ru.chernakov.appmonitor.presentation.info.InfoFragment
import ru.chernakov.appmonitor.presentation.list.ListFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        ApplicationModule::class,
        GlobalNavigationModule::class,
        LocalNavigationModule::class
    )
)
interface AppComponent {

    fun inject(appActivity: AppActivity)

    fun inject(infoFragment: InfoFragment)

    fun inject(listFragment: ListFragment)

}