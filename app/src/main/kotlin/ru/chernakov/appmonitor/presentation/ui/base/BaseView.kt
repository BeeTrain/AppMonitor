package ru.chernakov.appmonitor.presentation.ui.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseView : MvpView {

    fun setLoading(isLoading: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(msg: String)

}