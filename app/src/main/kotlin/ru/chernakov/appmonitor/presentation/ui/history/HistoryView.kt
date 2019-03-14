package ru.chernakov.appmonitor.presentation.ui.history

import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.presentation.ui.base.BaseView

interface HistoryView : BaseView {

    fun initAdapter(history: ArrayList<EventItem>)

}
