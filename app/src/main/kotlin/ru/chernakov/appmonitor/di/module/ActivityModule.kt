package ru.chernakov.appmonitor.di.module

import android.app.Activity
import dagger.Module
import dagger.Provides
import ru.chernakov.appmonitor.di.scope.PerActivity

@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

}