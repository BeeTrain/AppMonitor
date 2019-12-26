package ru.chernakov.appmonitor.presentation.ui.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import butterknife.OnClick
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_list.*
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.presentation.ui.base.BaseFragment
import ru.chernakov.appmonitor.presentation.ui.list.adapter.ListAdapter
import ru.chernakov.appmonitor.presentation.utils.ItemClickSupport
import ru.chernakov.appmonitor.presentation.utils.PackageUtils
import ru.chernakov.appmonitor.presentation.utils.ResourcesUtils
import javax.inject.Inject

class ListFragment : BaseFragment(), ListView, SearchView.OnQueryTextListener {
    override val layoutRes: Int = R.layout.fragment_list

    private var adapter: ListAdapter? = null

    @InjectPresenter
    lateinit var presenter: ListPresenter

    @Inject
    lateinit var loadApplications: LoadApplications

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        applicationsList.layoutManager = LinearLayoutManager(context)
        applicationsList.setHasFixedSize(true)
        adapter = activity?.let { ListAdapter(it, ArrayList()) }
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        menuInflater!!.inflate(R.menu.menu_list, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(this)
            searchView.setIconifiedByDefault(true)
            if (!TextUtils.isEmpty(presenter.query)) {
                searchView.setQuery(presenter.query, false)
            }
        }

        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun loadData() {
        presenter.loadApps()
    }

    override fun initAdapter(applications: ArrayList<ApplicationDto>) {
        applications.sortWith(Comparator { p1, p2 ->
            p1.name.toString().toLowerCase()
                .compareTo(p2.name.toString().toLowerCase())
        })

        adapter?.items = applications
        setLoading(false)
    }

    override fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)
        toolbar.navigationIcon = ResourcesUtils.getDrawable(R.drawable.ic_menu_open)
        toolbar.setNavigationOnClickListener(
            ListNavigationIconClickListener(
                activity,
                view.findViewById(R.id.scrollViewGrid),
                AccelerateDecelerateInterpolator(),
                ResourcesUtils.getDrawable(R.drawable.ic_menu_open),
                ResourcesUtils.getDrawable(R.drawable.ic_menu_close)
            )
        )
    }

    override fun setLoading(isLoading: Boolean) {
        progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        applicationsList.visibility = if (isLoading) View.GONE else View.VISIBLE
        backdropMenu.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.onQueryTextSubmit(adapter!!, query)

        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        presenter.onQueryTextChange(adapter!!, query)

        return false
    }

    @OnClick(R.id.btHistory)
    fun onHistoryClick() {
        presenter.goToHistory()
    }

    @OnClick(R.id.btService)
    fun onServiceClick() {
        presenter.toggleService(this)
    }

    @OnClick(R.id.btAbout)
    fun onAboutClick() {
        presenter.goToAbout()
    }

    @ProvidePresenter
    fun providePresenter(): ListPresenter {
        return ListPresenter(router, loadApplications)
    }
}