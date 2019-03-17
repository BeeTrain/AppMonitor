package ru.chernakov.appmonitor.presentation.ui.about

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_about.*
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.BuildConfig
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.presentation.ui.base.BaseFragment

class AboutFragment : BaseFragment(), AboutView {
    override val layoutRes: Int = R.layout.fragment_about

    @InjectPresenter
    lateinit var presenter: AboutPresenter

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
        updateUi()
    }

    override fun setLoading(isLoading: Boolean) {
        //
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    @ProvidePresenter
    fun providePresenter(): AboutPresenter {
        return AboutPresenter(router)
    }

    override fun updateUi() {
        tvAppVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)

    }

    private fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.title_about)
        toolbar.navigationIcon = activity.getDrawable(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            presenter.backToList()
        }
    }
}
