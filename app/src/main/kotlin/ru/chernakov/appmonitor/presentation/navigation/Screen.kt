package ru.chernakov.appmonitor.presentation.navigation

import android.support.v4.app.Fragment
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.ui.info.InfoFragment
import ru.chernakov.appmonitor.presentation.ui.list.ListFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screen {

    object List : SupportAppScreen() {
        override fun getFragment() = ListFragment()
    }

    object Info : SupportAppScreen() {
        override fun getFragment(): Fragment = InfoFragment()
    }

    data class ApplicationInfo(val applicationItem: ApplicationItem) : SupportAppScreen() {
        override fun getFragment(): Fragment = InfoFragment.create(applicationItem)
    }
}
