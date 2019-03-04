package ru.chernakov.appmonitor.presentation.list

import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.base.BaseView

interface ListView : BaseView {

    fun initAdapter(applications: List<ApplicationItem>)
}