package ru.chernakov.appmonitor.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.navigation.Screen
import ru.chernakov.appmonitor.presentation.service.PackageService
import ru.chernakov.appmonitor.presentation.ui.base.BaseActivity
import ru.chernakov.appmonitor.presentation.utils.AppUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class AppActivity : BaseActivity() {

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (intent.extras == null) {
                router.newRootScreen(Screen.List)
                router.navigateTo(Screen.List)
            } else {
                intent.extras?.let {
                    onProvidedIntent(intent)
                }
            }
        }


        actionBar?.setDisplayHomeAsUpEnabled(true)

        AppUtils.checkPermissions(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        router.backTo(Screen.List)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppUtils.UNINSTALL_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    App.appPreferences.isColdStart = true
                    restartActivity()
                }
            }
        }
    }

    private fun restartActivity() {
        val intent = Intent(applicationContext, AppActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        finish()
        startActivity(intent)
    }

    private fun onProvidedIntent(providedIntent: Intent) {
        val pack = providedIntent.extras!!.getString(AppUtils.INTENT_START_APP_INFO_SCREEN)
        if (pack != null && !pack.isEmpty()) {
            val serviceIntent = Intent(this, PackageService::class.java)
            serviceIntent.action = PackageService.ACTION_START
            App.instance.startService(serviceIntent)

            router.newRootScreen(Screen.List)
            router.navigateTo(Screen.ApplicationInfo(ApplicationItem(pack)))
        }
    }
}
