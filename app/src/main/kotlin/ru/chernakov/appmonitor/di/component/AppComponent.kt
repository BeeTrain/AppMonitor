package ru.chernakov.appmonitor.di.component

import dagger.Component
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.di.module.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface AppComponent {

    fun inject(app: App)

}