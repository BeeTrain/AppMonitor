package ru.chernakov.appmonitor.di.module

import dagger.Module
import dagger.Provides
import ru.chernakov.appmonitor.presentation.base.BaseActivity
import javax.inject.Singleton

@Module
class ActivityModule {

    @Provides
    @Singleton
    fun provideActivityContext(activity: BaseActivity): BaseActivity = activity

}