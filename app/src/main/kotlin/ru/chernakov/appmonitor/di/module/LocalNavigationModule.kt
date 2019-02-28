package ru.chernakov.appmonitor.di.module

import dagger.Module
import dagger.Provides
import ru.chernakov.appmonitor.di.scope.PerApplication
import ru.chernakov.appmonitor.navigation.LocalCiceroneHolder

@Module
class LocalNavigationModule {

    @Provides
    @PerApplication
    fun provideLocalNavigationHolder(): LocalCiceroneHolder {
        return LocalCiceroneHolder()
    }

}