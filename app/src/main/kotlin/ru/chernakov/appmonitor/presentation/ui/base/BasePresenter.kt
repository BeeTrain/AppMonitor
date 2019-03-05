package ru.chernakov.appmonitor.presentation.ui.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router


abstract class BasePresenter<View : MvpView>(protected var router: Router) : MvpPresenter<View>()
