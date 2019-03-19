package ru.chernakov.appmonitor.presentation.ui.list.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.dto.ApplicationDto
import ru.chernakov.appmonitor.presentation.utils.PackageUtils

class ListAdapterDelegate(private val activity: Activity) : AdapterDelegate<ArrayList<ApplicationDto>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ListViewHolder(activity.layoutInflater.inflate(R.layout.list_item_app, parent, false))
    }

    override fun isForViewType(items: ArrayList<ApplicationDto>, position: Int): Boolean {
        return items[position] is ApplicationDto
    }

    override fun onBindViewHolder(
        items: ArrayList<ApplicationDto>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val viewHolder: ListViewHolder = holder as ListViewHolder
        val item: ApplicationDto? = items[position]

        viewHolder.appName.text = item?.name
        viewHolder.appApk.text = item?.apk
        viewHolder.appIcon.setImageDrawable(PackageUtils.getPackageIcon(item?.apk))
    }

    internal inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var appName: TextView = itemView.findViewById(R.id.appName)
        var appApk: TextView = itemView.findViewById(R.id.appApk)
        var appIcon: ImageView = itemView.findViewById(R.id.appIcon)

    }
}