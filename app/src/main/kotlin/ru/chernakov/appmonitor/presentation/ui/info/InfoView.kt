package ru.chernakov.appmonitor.presentation.ui.info

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.model.OptionItem
import ru.chernakov.appmonitor.presentation.ui.base.BaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface InfoView : BaseView {

    fun initAdapter(optionItems: ArrayList<OptionItem>)

    fun initApplicationInfo(applicationItem: ApplicationItem)
}