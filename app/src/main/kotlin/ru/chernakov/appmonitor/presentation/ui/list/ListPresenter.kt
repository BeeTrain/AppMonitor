package ru.chernakov.appmonitor.presentation.ui.list

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.domain.interactor.base.observer.BaseObserver
import ru.chernakov.appmonitor.presentation.navigation.Screen
import ru.chernakov.appmonitor.presentation.ui.base.BasePresenter
import ru.chernakov.appmonitor.presentation.ui.list.adapter.ListAdapter
import ru.terrakok.cicerone.Router

@InjectViewState
class ListPresenter(router: Router, val loadApplicationsUseCase: LoadApplications) : BasePresenter<ListView>(router) {

    val appList = ArrayList<ApplicationDto>()

    val query: String? = ""

    fun goToInfo(applicationItem: ApplicationItem) {
        router.navigateTo(Screen.ApplicationInfo(applicationItem))
    }

    fun goToHistory() {
        router.navigateTo(Screen.History)
    }

    fun goToAbout() {
        router.navigateTo(Screen.About)
    }

    fun loadApps() {
        viewState.setLoading(true)
        loadApplicationsUseCase.execute(ListObserver(), null)
    }

    fun onQueryTextChange(adapter: ListAdapter, query: String?) {
        adapter.filter.filter(query)
    }

    fun onQueryTextSubmit(adapter: ListAdapter, query: String?) {
        adapter.filter.filter(query)
    }

    private inner class ListObserver : BaseObserver<List<ApplicationDto>>() {

        override fun onComplete() {
            viewState.setLoading(false)
        }

        override fun onNext(t: List<ApplicationDto>) {
            appList.clear()
            appList.addAll(t)

            viewState.initAdapter(appList)
        }

        override fun onError(e: Throwable) {
            viewState.setLoading(false)
            viewState.showMessage(e.message!!)
        }
    }
}