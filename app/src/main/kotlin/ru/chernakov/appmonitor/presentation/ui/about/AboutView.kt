package ru.chernakov.appmonitor.presentation.ui.about

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.chernakov.appmonitor.presentation.ui.base.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface AboutView : BaseView {

    fun updateUi()

}
