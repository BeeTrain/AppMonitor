package ru.chernakov.appmonitor.presentation.list

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.domain.interactor.BaseObserver
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.navigation.Screen
import ru.chernakov.appmonitor.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class ListPresenter(router: Router, val loadApplicationsUseCase: LoadApplications) : BasePresenter<ListView>(router) {

    val appList = ArrayList<ApplicationItem>()

    fun goToInfo(applicationItem: ApplicationItem) {
        router.navigateTo(Screen.ApplicationInfo(applicationItem))
    }

    fun loadApps() {
        if (appList.size == 0) {
            viewState.setLoading(true)
            loadApplicationsUseCase.execute(ListObserver(), null)
        }
    }

    private inner class ListObserver : BaseObserver<List<ApplicationItem>>() {

        override fun onComplete() {
            viewState.setLoading(false)
        }

        override fun onNext(t: List<ApplicationItem>) {
            appList.clear()
            viewState.initAdapter(t)
        }

        override fun onError(e: Throwable) {
            viewState.setLoading(false)
            e.message?.let { viewState.showMessage(it) }
        }
    }
}