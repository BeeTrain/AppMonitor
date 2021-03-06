package ru.chernakov.appmonitor.presentation.ui.history

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_history.*
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.domain.interactor.LoadHistory
import ru.chernakov.appmonitor.presentation.ui.base.BaseFragment
import ru.chernakov.appmonitor.presentation.ui.history.adapter.HistoryAdapter
import ru.chernakov.appmonitor.presentation.utils.ItemClickSupport
import java.util.*
import javax.inject.Inject


class HistoryFragment : BaseFragment(), HistoryView {
    override val layoutRes = ru.chernakov.appmonitor.R.layout.fragment_history

    @InjectPresenter
    lateinit var presenter: HistoryPresenter

    @Inject
    lateinit var loadHistory: LoadHistory

    private var adapter: HistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun loadData() {
        presenter.loadHistory()
    }

    override fun initAdapter(history: ArrayList<EventItem>) {
        if (history.size > 0) {
            content.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE

            history.sortWith(Comparator { p1, p2 ->
                p2.actionDate.toString().toLowerCase()
                    .compareTo(p1.actionDate.toString().toLowerCase())
            })

            adapter = activity?.let { HistoryAdapter(it, history) }
            historyList.layoutManager = LinearLayoutManager(context)
            historyList.adapter = adapter

            with(ItemClickSupport.addTo(historyList)) {
                setOnItemClickListener { _, position, _ ->
                    adapter?.getItem(position)?.let {
                        presenter.onEventClick(it)
                    }
                }
            }

            adapter?.notifyDataSetChanged()
            setLoading(false)
        } else {
            content.visibility = View.GONE
            tvNoData.visibility = View.VISIBLE
        }
    }

    @ProvidePresenter
    fun providePresenter(): HistoryPresenter {
        return HistoryPresenter(router, loadHistory)
    }

    override fun setUpToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(ru.chernakov.appmonitor.R.id.toolbar)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.title_history)
        toolbar.navigationIcon = activity.getDrawable(ru.chernakov.appmonitor.R.drawable.ic_close)
        toolbar.setNavigationOnClickListener {
            presenter.backToList()
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