package ru.chernakov.appmonitor.presentation.list

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.navigation.Screen
import ru.chernakov.appmonitor.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class ListPresenter(router: Router) : BasePresenter<ListView>(router) {

    fun goToInfo() {
        router.navigateTo(Screen.Info)
    }

}