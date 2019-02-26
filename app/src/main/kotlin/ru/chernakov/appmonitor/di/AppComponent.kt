package ru.chernakov.appmonitor.di

import dagger.Component
import ru.chernakov.appmonitor.di.module.ActivityModule
import ru.chernakov.appmonitor.presentation.info.InfoPresenter
import ru.chernakov.appmonitor.presentation.list.ListPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [ActivityModule::class])
interface AppComponent {

    fun inject(listPresenter: ListPresenter)

    fun inject(infoPresenter: InfoPresenter)

}