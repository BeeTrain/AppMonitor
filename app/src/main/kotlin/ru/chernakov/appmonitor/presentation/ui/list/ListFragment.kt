package ru.chernakov.appmonitor.presentation.ui.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import butterknife.OnClick
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_list.*
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.UIThread
import ru.chernakov.appmonitor.data.cache.ApplicationCache
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.domain.executor.ThreadExecutor
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.presentation.service.PackageService
import ru.chernakov.appmonitor.presentation.ui.base.BaseFragment
import ru.chernakov.appmonitor.presentation.ui.list.adapter.ListAdapter
import ru.chernakov.appmonitor.presentation.utils.ItemClickSupport
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import ru.chernakov.appmonitor.presentation.utils.ResourcesUtils
import javax.inject.Inject

class ListFragment : BaseFragment(), ListView {
    override val layoutRes: Int = R.layout.fragment_list

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
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = super.onCreateView(inflater, container, savedInstanceState)
        setUpToolbar(v!!)

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        menuInflater!!.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_refresh -> {
                presenter.loadApps()
                showMessage(getString(R.string.msg_list_refreshing))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun loadData() {
        presenter.loadApps()
    }

    override fun initAdapter(applications: ArrayList<ApplicationDto>) {
        applications.sortWith(Comparator { p1, p2 ->
            p1.name.toString().toLowerCase()
                .compareTo(p2.name.toString().toLowerCase())
        })

        adapter = activity?.let { ListAdapter(it, applications) }
        applicationsList.layoutManager = LinearLayoutManager(context)
        applicationsList.adapter = adapter

        with(ItemClickSupport.addTo(applicationsList)) {
            setOnItemClickListener { _, position, _ ->
                adapter?.getItem(position)?.let {
                    val applicationItem = PackageUtils.getApplicationItem(it.apk)
                    if (applicationItem != null) {
                        presenter.goToInfo(applicationItem)
                    }
                }
            }
        }

        adapter?.notifyDataSetChanged()
        setLoading(false)
    }

    override fun setLoading(isLoading: Boolean) {
        progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        applicationsList.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    @OnClick(R.id.btHistory)
    fun onHistoryClick() {
        presenter.goToHistory()
    }

    @OnClick(R.id.btService)
    fun onServiceClick() {
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

    @OnClick(R.id.btAbout)
    fun onAboutClick() {
        presenter.goToAbout()
    }

    private fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)
        toolbar.navigationIcon = ResourcesUtils.getDrawable(R.drawable.ic_menu_open)
        toolbar.setNavigationOnClickListener(
            NavigationIconClickListener(
                activity,
                view.findViewById(R.id.scrollViewGrid),
                AccelerateDecelerateInterpolator(),
                ResourcesUtils.getDrawable(R.drawable.ic_menu_open),
                ResourcesUtils.getDrawable(R.drawable.ic_menu_close)
            )
        )
    }

    @ProvidePresenter
    fun providePresenter(): ListPresenter {
        return ListPresenter(
            router,
            LoadApplications(ApplicationRepository(applicationCache), threadExecutor, uiThread)
        )
    }
}