package ru.chernakov.appmonitor.presentation.info

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.OnClick
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.toolbar_application_info.*
import ru.chernakov.appmonitor.App

import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.presentation.base.BaseFragment

class InfoFragment : BaseFragment(), InfoView {

    override val layoutRes = R.layout.fragment_info

    @InjectPresenter
    lateinit var presenter: InfoPresenter

    var applicationItem: ApplicationItem? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = super.onCreateView(inflater, container, savedInstanceState)
        setUpToolbar(v!!)

        return v
    }

    override fun loadData() {
        appIcon.setImageDrawable(applicationItem?.icon)
        appName.text = applicationItem?.name
        appVersion.text = applicationItem?.version
        appPackage.text = applicationItem?.apk
    }

    @ProvidePresenter
    fun providePresenter(): InfoPresenter {
        return InfoPresenter(router)
    }

    @OnClick(ru.chernakov.appmonitor.R.id.text)
    fun goToNext() {
        applicationItem?.let { presenter.copyFile(it) }
    }

    private fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.navigationIcon = activity?.getDrawable(R.drawable.ic_arrow_back)
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            presenter.goToList()
        }
    }

    override fun setLoading(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}