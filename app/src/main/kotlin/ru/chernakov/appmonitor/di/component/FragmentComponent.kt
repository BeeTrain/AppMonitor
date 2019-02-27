package ru.chernakov.appmonitor.di.component

import dagger.Component
import ru.chernakov.appmonitor.di.module.FragmentModule

@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

}