package ru.chernakov.appmonitor.presentation.info

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.navigation.Screen
import ru.chernakov.appmonitor.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class InfoPresenter(router: Router) : BasePresenter<InfoView>(router) {

    fun goToList() {
        router.navigateTo(Screen.List)
    }

}