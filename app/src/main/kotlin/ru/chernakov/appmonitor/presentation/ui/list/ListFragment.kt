package ru.chernakov.appmonitor.presentation.ui.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_list.*
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.UIThread
import ru.chernakov.appmonitor.data.cache.ApplicationCache
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.presentation.service.PackageService
import ru.chernakov.appmonitor.presentation.ui.base.BaseFragment
import ru.chernakov.appmonitor.presentation.ui.list.adapter.ListAdapter
import ru.chernakov.appmonitor.presentation.utils.ItemClickSupport
import ru.chernakov.appmonitor.presentation.utils.ResourcesUtils
import javax.inject.Inject


class ListFragment : BaseFragment(), ListView {
    override val layoutRes = ru.chernakov.appmonitor.R.layout.fragment_list

    private var adapter: ListAdapter? = null

    @InjectPresenter
    lateinit var presenter: ListPresenter

    @Inject
    lateinit var uiThread: UIThread

    @Inject
    lateinit var threadExecutor: ThreadExecutor

    @Inject
    lateinit var applicationCache: ApplicationCache

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = super.onCreateView(inflater, container, savedInstanceState)

        setUpToolbar(v!!)

        presenter.loadApps()

        return v
    }

    override fun loadData() {

        swipeRefresh.setOnRefreshListener { presenter.loadApps() }
    }

    override fun initAdapter(applications: List<ApplicationItem>) {
        swipeRefresh.isRefreshing = false

        adapter = activity?.let { ListAdapter(it, applications) }
        applicationsList.layoutManager = LinearLayoutManager(context)
        applicationsList.adapter = adapter

        with(ItemClickSupport.addTo(applicationsList)) {
            setOnItemClickListener { recyclerView, position, v ->
                adapter?.getItem(position)?.let {
                    presenter.goToInfo(it)
                }
            }
        }

        adapter?.notifyDataSetChanged()
        setLoading(false)
    }

    @ProvidePresenter
    fun providePresenter(): ListPresenter {
        return ListPresenter(
            router,
            LoadApplications(ApplicationRepository(applicationCache), threadExecutor, uiThread)
        )
    }

    private fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(ru.chernakov.appmonitor.R.id.toolbar)
        toolbar.navigationIcon = ResourcesUtils.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_cloud)

        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            if (App.isServiceRunning) {
                showMessage(getString(R.string.msg_service_stopped))
                val intent = Intent(context, PackageService::class.java)
                intent.action = PackageService.ACTION_STOP
                App.instance.startService(intent)
            } else {
                showMessage(getString(R.string.msg_service_started))
                val intent = Intent(context, PackageService::class.java)
                intent.action = PackageService.ACTION_START
                App.instance.startService(intent)
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        scrollViewGrid.visibility = if (isLoading) View.GONE else View.VISIBLE
        progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
