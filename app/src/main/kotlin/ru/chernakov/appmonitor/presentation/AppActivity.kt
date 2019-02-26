package ru.chernakov.appmonitor.presentation

import android.os.Bundle
import ru.chernakov.appmonitor.presentation.base.BaseActivity
import ru.chernakov.appmonitor.presentation.list.ListFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppActivity @Inject
constructor() : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigateTo(ListFragment(), true)
        }
    }
}
