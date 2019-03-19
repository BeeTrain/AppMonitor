package ru.chernakov.appmonitor.presentation.ui.history

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.presentation.ui.base.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface HistoryView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun initAdapter(history: ArrayList<EventItem>)

}
