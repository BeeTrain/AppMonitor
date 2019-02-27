package ru.chernakov.appmonitor.di.scope

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PerApplication {
}