package ru.chernakov.appmonitor.navigation

import android.support.v4.app.Fragment
import ru.chernakov.appmonitor.presentation.info.InfoFragment
import ru.chernakov.appmonitor.presentation.list.ListFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screen {

    object List : SupportAppScreen() {
        override fun getFragment() = ListFragment()
    }

    object Info : SupportAppScreen() {
        override fun getFragment(): Fragment = InfoFragment()
    }
}