package ru.chernakov.appmonitor.presentation.ui.history.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.EventItem
import ru.chernakov.appmonitor.presentation.utils.DateUtils
import java.util.*

class HistoryAdapterDelegate(private val activity: Activity) : AdapterDelegate<List<EventItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ListViewHolder(activity.layoutInflater.inflate(R.layout.list_item_event, parent, false))
    }

    override fun isForViewType(items: List<EventItem>, position: Int): Boolean {
        return items[position] is EventItem
    }

    override fun onBindViewHolder(
        items: List<EventItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val viewHolder: ListViewHolder = holder as ListViewHolder
        val item: EventItem? = items[position]

        when (item?.action) {
            EventItem.EVENT_INSTALL -> {
                viewHolder.action.text = activity.getString(R.string.msg_app_installed)
                viewHolder.actionDate.text =
                    activity.getString(R.string.install_date, DateUtils.formatDate(item.actionDate?.let { Date(it) }))

            }
            EventItem.EVENT_UPDATE -> {
                viewHolder.action.text = activity.getString(R.string.msg_app_updated)
                viewHolder.actionDate.text =
                    activity.getString(R.string.update_date, DateUtils.formatDate(item.actionDate?.let { Date(it) }))

            }
            EventItem.EVENT_UNINSTALL -> {
                viewHolder.action.text = activity.getString(R.string.msg_app_uninstalled)
                viewHolder.actionDate.text =
                    activity.getString(R.string.uninstall_date, DateUtils.formatDate(item.actionDate?.let { Date(it) }))
            }
        }

        viewHolder.appIcon.setImageDrawable(item?.icon)
        viewHolder.appName.text = item?.name
        viewHolder.appVersion.text = item?.version
        viewHolder.actionDate.text = DateUtils.formatDate(item?.actionDate?.let { Date(it) })
    }

    internal inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        var action: TextView = itemView.findViewById(R.id.action)
        var appName: TextView = itemView.findViewById(R.id.appName)
        var appVersion: TextView = itemView.findViewById(R.id.appVersion)
        var actionDate: TextView = itemView.findViewById(R.id.actionDate)

    }
}
