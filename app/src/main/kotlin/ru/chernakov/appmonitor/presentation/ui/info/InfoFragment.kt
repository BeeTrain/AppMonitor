package ru.chernakov.appmonitor.presentation.ui.info

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.toolbar_application_info.*
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.model.OptionItem
import ru.chernakov.appmonitor.presentation.ui.base.BaseFragment
import ru.chernakov.appmonitor.presentation.ui.info.adapter.OptionsAdapter
import ru.chernakov.appmonitor.presentation.utils.DateUtils
import java.util.*

class InfoFragment : BaseFragment(), InfoView {

    override val layoutRes = R.layout.fragment_info

    @InjectPresenter
    lateinit var presenter: InfoPresenter

    lateinit var applicationItem: ApplicationItem

    private var adapter: OptionsAdapter? = null

    companion object {
        fun create(appItem: ApplicationItem) =
            InfoFragment().apply {
                applicationItem = appItem
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun loadData() {
        initApplicationInfo(presenter.applicationItem)
        initAdapter(presenter.initOptionsList())
    }

    override fun initApplicationInfo(applicationItem: ApplicationItem) {
        appIcon.setImageDrawable(applicationItem.icon)
        appName.text = applicationItem.name
        appVersion.text = applicationItem.version
        appPackage.text = applicationItem.apk
        appSHA.text = getString(R.string.value_sha, applicationItem.sha)
        appInstallDate.text =
            getString(R.string.value_installed, DateUtils.formatDate(applicationItem.installDate?.let { Date(it) }))
        appUpdateDate.text =
            getString(R.string.value_updated, DateUtils.formatDate(applicationItem.updateDate?.let { Date(it) }))
    }

    override fun initAdapter(optionItems: ArrayList<OptionItem>) {
        activity?.let {
            adapter = OptionsAdapter(it, optionItems)
            optionsList.layoutManager = LinearLayoutManager(context)
            optionsList.adapter = adapter
            adapter?.notifyDataSetChanged()
            presenter.setOnClickListener(optionsList, adapter!!, it)
        }
    }

    @ProvidePresenter
    fun providePresenter(): InfoPresenter {
        return InfoPresenter(router, applicationItem)
    }

    override fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.title_info)
        toolbar.navigationIcon = activity.getDrawable(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            presenter.backToList()
        }
    }

    override fun setLoading(isLoading: Boolean) {
        //
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}