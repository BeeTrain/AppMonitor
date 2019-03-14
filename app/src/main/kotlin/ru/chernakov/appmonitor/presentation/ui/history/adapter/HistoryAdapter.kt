package ru.chernakov.appmonitor.presentation.ui.history.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import ru.chernakov.appmonitor.data.model.EventItem

class HistoryAdapter(activity: FragmentActivity, private val items: List<EventItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val delegatesManager = AdapterDelegatesManager<List<EventItem>>()

    init {
        delegatesManager.addDelegate(HistoryAdapterDelegate(activity))
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(viewGroup, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, position, viewHolder)
    }

    fun getItem(position: Int): EventItem {
        return items[position]
    }
}
