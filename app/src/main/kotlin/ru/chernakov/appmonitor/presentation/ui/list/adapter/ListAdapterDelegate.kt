package ru.chernakov.appmonitor.presentation.ui.list.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.ApplicationItem

class ListAdapterDelegate(private val activity: Activity) : AdapterDelegate<List<ApplicationItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ListViewHolder(activity.layoutInflater.inflate(R.layout.list_item_app, parent, false))
    }

    override fun isForViewType(items: List<ApplicationItem>, position: Int): Boolean {
        return items[position] is ApplicationItem
    }

    override fun onBindViewHolder(
        items: List<ApplicationItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val viewHolder: ListViewHolder = holder as ListViewHolder
        val item: ApplicationItem? = items[position]

        viewHolder.appName.text = item?.name
        viewHolder.appApk.text = item?.apk
        viewHolder.appIcon.setImageDrawable(item?.icon)
    }

    internal inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var appName: TextView = itemView.findViewById(R.id.appName)
        var appApk: TextView = itemView.findViewById(R.id.appApk)
        var appIcon: ImageView = itemView.findViewById(R.id.appIcon)

    }
}