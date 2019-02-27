package ru.chernakov.appmonitor.presentation.list

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.presentation.AppActivity
import ru.chernakov.appmonitor.presentation.base.BasePresenter
import ru.chernakov.appmonitor.presentation.info.InfoFragment

@InjectViewState
class ListPresenter(var activity: AppActivity) : BasePresenter<ListView>() {

    fun goTo() {
        activity.navigateTo(InfoFragment(), true)
    }

}