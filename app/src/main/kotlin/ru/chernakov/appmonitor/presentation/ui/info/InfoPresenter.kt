package ru.chernakov.appmonitor.presentation.ui.info

import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.InjectViewState
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.model.OptionItem
import ru.chernakov.appmonitor.presentation.navigation.Screen
import ru.chernakov.appmonitor.presentation.ui.base.BasePresenter
import ru.chernakov.appmonitor.presentation.ui.info.adapter.OptionsAdapter
import ru.chernakov.appmonitor.presentation.utils.AppUtils
import ru.chernakov.appmonitor.presentation.utils.ItemClickSupport
import ru.chernakov.appmonitor.presentation.utils.ResourcesUtils
import ru.terrakok.cicerone.Router


@InjectViewState
class InfoPresenter(router: Router, val applicationItem: ApplicationItem) : BasePresenter<InfoView>(router) {

    private val optionsList = ArrayList<OptionItem>()

    fun backToList() {
        router.backTo(Screen.List)
    }

    fun initOptionsList(): ArrayList<OptionItem> {
        optionsList.clear()

        optionsList.add(
            OptionItem(
                OptionItem.OPEN_APP_ID,
                ResourcesUtils.getString(R.string.option_title_open_app),
                ResourcesUtils.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_open)
            )
        )

        when (AppUtils.getOutputFilename(applicationItem).exists()) {
            true -> optionsList.add(
                OptionItem(
                    OptionItem.APK_SAVED_ID,
                    ResourcesUtils.getString(R.string.option_title_apk_saved),
                    ResourcesUtils.getDrawable(R.drawable.ic_apk_saved)
                )
            )
            false -> optionsList.add(
                OptionItem(
                    OptionItem.SAVE_APK_ID,
                    ResourcesUtils.getString(R.string.option_title_save_apk),
                    ResourcesUtils.getDrawable(R.drawable.ic_save_apk)
                )
            )
        }
        if (applicationItem.fromPlayMarket!!) {
            optionsList.add(
                OptionItem(
                    OptionItem.PLAY_MARKET_ID,
                    ResourcesUtils.getString(R.string.option_title_open_play_market),
                    ResourcesUtils.getDrawable(R.drawable.ic_shop)
                )
            )
        }
        optionsList.add(
            OptionItem(
                OptionItem.DELETE_APP_ID,
                ResourcesUtils.getString(R.string.option_title_delete_app),
                ResourcesUtils.getDrawable(R.drawable.ic_uninstall)
            )
        )

        return optionsList
    }

    fun setOnClickListener(recyclerView: RecyclerView, adapter: OptionsAdapter, activity: FragmentActivity) {
        with(ItemClickSupport.addTo(recyclerView)) {
            setOnItemClickListener { _, position, _ ->
                when (adapter.getItem(position).id) {
                    OptionItem.OPEN_APP_ID -> startApplication(activity)
                    OptionItem.SAVE_APK_ID -> saveApk(activity)
                    OptionItem.PLAY_MARKET_ID -> openPlayMarket(activity)
                    OptionItem.DELETE_APP_ID -> uninstallApplication(activity)
                }
            }
        }
    }

    private fun startApplication(activity: FragmentActivity) {
        try {
            val intent = activity.packageManager.getLaunchIntentForPackage(applicationItem.apk!!)
            activity.startActivity(intent)
        } catch (e: NullPointerException) {
            viewState.showMessage(ResourcesUtils.getString(R.string.error_cannot_run_app))
        }
    }

    private fun saveApk(activity: FragmentActivity) {
        if (AppUtils.checkPermissions(activity)!!) {
            when (AppUtils.copyFile(applicationItem)!!) {
                true -> {
                    viewState.showMessage(ResourcesUtils.getString(R.string.msg_apk_saved))
                    viewState.initAdapter(initOptionsList())
                }
                false -> viewState.showMessage(ResourcesUtils.getString(R.string.error_cannot_save_apk))
            }
        }
    }

    private fun openPlayMarket(activity: FragmentActivity) {
        applicationItem.apk?.let { AppUtils.goToGooglePlay(activity, it) }
    }

    private fun uninstallApplication(activity: FragmentActivity) {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
        intent.data = Uri.parse("package:" + applicationItem.apk)
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        activity.startActivityForResult(intent, AppUtils.UNINSTALL_REQUEST_CODE)
    }
}