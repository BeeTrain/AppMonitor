package ru.chernakov.appmonitor.presentation.list

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.presentation.base.BaseActivity
import ru.chernakov.appmonitor.presentation.base.BasePresenter
import ru.chernakov.appmonitor.presentation.info.InfoFragment
import javax.inject.Inject

@InjectViewState
class ListPresenter : BasePresenter<ListView>() {


    @Inject
    lateinit var activity: BaseActivity

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        App.appComponent.inject(this)
    }

    fun goTo() {
        activity.navigateTo(InfoFragment(), true)
    }

}