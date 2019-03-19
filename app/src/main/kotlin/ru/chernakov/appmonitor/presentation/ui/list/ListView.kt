package ru.chernakov.appmonitor.presentation.ui.list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.presentation.ui.base.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface ListView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun initAdapter(applications: ArrayList<ApplicationDto>)
}