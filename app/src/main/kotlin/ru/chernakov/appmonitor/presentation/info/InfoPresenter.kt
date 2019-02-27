package ru.chernakov.appmonitor.presentation.info

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.presentation.AppActivity
import ru.chernakov.appmonitor.presentation.base.BasePresenter

@InjectViewState
class InfoPresenter(var activity: AppActivity) : BasePresenter<InfoView>() {

}