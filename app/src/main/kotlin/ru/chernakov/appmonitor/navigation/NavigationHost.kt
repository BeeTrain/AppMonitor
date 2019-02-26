package ru.chernakov.appmonitor.navigation

import android.support.v4.app.Fragment

interface NavigationHost {
    fun navigateTo(fragment: Fragment, addToBackStack: Boolean)
}