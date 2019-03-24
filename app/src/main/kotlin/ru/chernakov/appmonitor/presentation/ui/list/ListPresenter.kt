package ru.chernakov.appmonitor.presentation.ui.list

import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.domain.interactor.base.observer.BaseObserver
import ru.chernakov.appmonitor.presentation.navigation.Screen
import ru.chernakov.appmonitor.presentation.service.PackageService
import ru.chernakov.appmonitor.presentation.ui.base.BaseFragment
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
        if (appList.size > 0) {
            viewState.initAdapter(appList)
        } else {
            loadApplicationsUseCase.execute(ListObserver(), null)
        }
    }

    fun toggleService(fragment: BaseFragment) {
        if (App.isServiceRunning) {
            viewState.showMessage(fragment.getString(R.string.msg_service_stopped))
            val intent = Intent(fragment.context, PackageService::class.java)
            intent.action = PackageService.ACTION_STOP
            App.instance.startService(intent)
        } else {
            viewState.showMessage(fragment.getString(R.string.msg_service_started))
            val intent = Intent(fragment.context, PackageService::class.java)
            intent.action = PackageService.ACTION_START
            App.instance.startService(intent)
        }
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