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
class InfoPresenter(router: Router, private val applicationItem: ApplicationItem) : BasePresenter<InfoView>(router) {

    private val optionsList = ArrayList<OptionItem>()

    fun backToList() {
        router.backTo(Screen.List)
    }

    fun initOptionsList(): ArrayList<OptionItem> {
        if (optionsList.isEmpty()) {

            optionsList.add(
                OptionItem(
                    0,
                    ResourcesUtils.getString(R.string.option_title_open_app),
                    ResourcesUtils.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_open)
                )
            )
            optionsList.add(
                OptionItem(
                    1,
                    ResourcesUtils.getString(R.string.option_title_save_apk),
                    ResourcesUtils.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_file_download)
                )
            )
            optionsList.add(
                OptionItem(
                    2,
                    ResourcesUtils.getString(R.string.option_title_open_play_market),
                    ResourcesUtils.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_play_circle)
                )
            )
            optionsList.add(
                OptionItem(
                    3,
                    ResourcesUtils.getString(R.string.option_title_delete_app),
                    ResourcesUtils.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_uninstall)
                )
            )
        }
        return optionsList
    }

    fun setOnClickListener(recyclerView: RecyclerView, adapter: OptionsAdapter, activity: FragmentActivity) {
        with(ItemClickSupport.addTo(recyclerView)) {
            setOnItemClickListener { _, position, _ ->
                when (adapter.getItem(position).id) {
                    0 -> startApplication(activity)
                    1 -> saveApk()
                    2 -> openPlayMarket(activity)
                    3 -> uninstallApplication(activity)
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

    private fun saveApk() {
        if (AppUtils.copyFile(applicationItem)!!) {
            viewState.showMessage(ResourcesUtils.getString(R.string.apk_saved))
        } else {
            viewState.showMessage(ResourcesUtils.getString(R.string.error_cannot_save_apk))
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