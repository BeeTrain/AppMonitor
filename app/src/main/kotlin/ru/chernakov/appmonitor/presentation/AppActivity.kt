package ru.chernakov.appmonitor.presentation

import android.os.Bundle
import ru.chernakov.appmonitor.di.component.ActivityComponent
import ru.chernakov.appmonitor.di.component.DaggerActivityComponent
import ru.chernakov.appmonitor.di.module.ActivityModule
import ru.chernakov.appmonitor.presentation.base.BaseActivity
import ru.chernakov.appmonitor.presentation.list.ListFragment
import javax.inject.Inject
import javax.inject.Singleton

class AppActivity @Inject
constructor() : BaseActivity() {

    companion object {
        lateinit var component: ActivityComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

        if (savedInstanceState == null) {
            navigateTo(ListFragment(), true)
        }
    }

    private fun inject() {
        component = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        component.inject(this)
    }

    fun getActivityComponent(): ActivityComponent {
        return component
    }
}
