package ru.chernakov.appmonitor.presentation.ui.history

import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.domain.interactor.BaseObserver
import ru.chernakov.appmonitor.domain.interactor.LoadHistory
import ru.chernakov.appmonitor.presentation.navigation.Screen
import ru.chernakov.appmonitor.presentation.ui.base.BasePresenter
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import ru.chernakov.appmonitor.presentation.utils.ResourcesUtils
import ru.terrakok.cicerone.Router

@InjectViewState
class HistoryPresenter(router: Router, private val loadHistoryUseCase: LoadHistory) :
    BasePresenter<HistoryView>(router) {

    val historyList = ArrayList<EventItem>()

    fun backToList() {
        router.backTo(Screen.List)
    }

    fun goToInfo(applicationItem: ApplicationItem) {
        router.navigateTo(Screen.ApplicationInfo(applicationItem))
    }

    fun onEventClick(eventItem: EventItem) {
        val applicationItem = PackageUtils.getApplicationItem(eventItem.apk)
        if (applicationItem != null) {
            goToInfo(applicationItem)
        } else {
            viewState.showMessage(ResourcesUtils.getString(R.string.msg_app_wasnt_found))
        }
    }

    fun loadHistory() {
        viewState.setLoading(true)
        loadHistoryUseCase.execute(HistoryObserver(), null)
    }

    private inner class HistoryObserver : BaseObserver<List<EventItem>>() {

        override fun onNext(t: List<EventItem>) {
            historyList.clear()
            historyList.addAll(t)

            viewState.initAdapter(historyList)
        }

        override fun onComplete() {
            viewState.setLoading(false)
        }

        override fun onError(e: Throwable) {
            viewState.setLoading(false)
            viewState.showMessage(e.message!!)
        }
    }
}
