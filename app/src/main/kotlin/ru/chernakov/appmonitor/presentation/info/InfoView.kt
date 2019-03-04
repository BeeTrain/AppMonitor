package ru.chernakov.appmonitor.presentation.info

import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.model.OptionItem
import ru.chernakov.appmonitor.presentation.base.BaseView

interface InfoView : BaseView {

    fun initAdapter(optionItems: ArrayList<OptionItem>)

    fun initApplicationInfo(applicationItem: ApplicationItem)
}