package ru.chernakov.appmonitor.presentation.list

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
import ru.chernakov.appmonitor.data.model.ApplicationItem
import ru.chernakov.appmonitor.data.repository.ApplicationRepository
import ru.chernakov.appmonitor.data.utils.ItemClickSupport
import ru.chernakov.appmonitor.domain.interactor.LoadApplications
import ru.chernakov.appmonitor.presentation.base.BaseFragment
import ru.chernakov.appmonitor.presentation.list.adapter.ListAdapter


class ListFragment : BaseFragment(), ListView {
    override val layoutRes = ru.chernakov.appmonitor.R.layout.fragment_list

    private var adapter: ListAdapter? = null

    @InjectPresenter
    lateinit var presenter: ListPresenter

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
        presenter.loadApps()

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            presenter.loadApps()
        }
    }

    override fun setupListAdapter(applications: ArrayList<ApplicationItem>) {
        adapter = activity?.let { ListAdapter(it, applications) }
        applicationsList.layoutManager = LinearLayoutManager(context)
        applicationsList.adapter = adapter
        adapter?.notifyDataSetChanged()

        with(ItemClickSupport.addTo(applicationsList)) {
            setOnItemClickListener { recyclerView, position, v ->
                adapter?.getItem(position)?.let { presenter.goToInfo(it) }
            }
        }
    }

    @ProvidePresenter
    fun providePresenter(): ListPresenter {
        return activity?.let { ApplicationRepository(it) }?.let { LoadApplications(it) }?.let {
            ListPresenter(
                router,
                it
            )
        }!!
    }

    private fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.navigationIcon = activity?.getDrawable(R.drawable.ic_cloud)
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)
    }

    override fun setLoading(isLoading: Boolean) {
        scrollViewGrid.visibility = if (isLoading) View.GONE else View.VISIBLE
        progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        swipeRefresh.isRefreshing = isLoading
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
