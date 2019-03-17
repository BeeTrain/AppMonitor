package ru.chernakov.appmonitor.presentation.ui.about

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.presentation.navigation.Screen
import ru.chernakov.appmonitor.presentation.ui.base.BasePresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class AboutPresenter(router: Router) : BasePresenter<AboutView>(router) {

    fun backToList() {
        router.backTo(Screen.List)
    }

}
