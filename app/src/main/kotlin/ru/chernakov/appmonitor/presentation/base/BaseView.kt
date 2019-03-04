package ru.chernakov.appmonitor.presentation.base

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    fun setLoading(isLoading: Boolean)

    fun showMessage(msg: String)

}