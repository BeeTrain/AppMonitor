package ru.chernakov.appmonitor.presentation.ui.list

import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.presentation.ui.base.BaseView

interface ListView : BaseView {

    fun initAdapter(applications: ArrayList<ApplicationDto>)
}