package ru.chernakov.appmonitor.presentation.info

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.utils.AppUtils
import ru.chernakov.appmonitor.navigation.Screen
import ru.chernakov.appmonitor.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class InfoPresenter(router: Router) : BasePresenter<InfoView>(router) {

    fun goToList() {
        router.navigateTo(Screen.List)
    }

    fun copyFile(applicationItem: ApplicationItem) {
        if (AppUtils.copyFile(applicationItem)!!) {
            viewState.showMessage("Finished")
        } else {
            viewState.showMessage("Failed")

        }
    }

}