package ru.chernakov.appmonitor.di.component

import dagger.Component
import ru.chernakov.appmonitor.di.module.ActivityModule
import ru.chernakov.appmonitor.di.scope.PerApplication
import ru.chernakov.appmonitor.presentation.AppActivity
import ru.chernakov.appmonitor.presentation.list.ListPresenter

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(appActivity: AppActivity)

    fun inject(listPresenter: ListPresenter)

}