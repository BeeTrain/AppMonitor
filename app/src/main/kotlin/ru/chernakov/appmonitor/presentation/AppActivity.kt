package ru.chernakov.appmonitor.presentation

import android.os.Bundle
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.utils.AppUtils
import ru.chernakov.appmonitor.navigation.Screen
import ru.chernakov.appmonitor.presentation.base.BaseActivity
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class AppActivity : BaseActivity() {

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            router.newRootScreen(Screen.List)
            router.navigateTo(Screen.List)
        }

        actionBar?.setDisplayHomeAsUpEnabled(true)

        AppUtils.checkPermissions(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        router.backTo(Screen.List)
    }
}
