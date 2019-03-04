package ru.chernakov.appmonitor.presentation.info.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.data.model.OptionItem

class OptionsAdapterDelegate(private val activity: Activity) : AdapterDelegate<List<OptionItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return OptionViewHolder(activity.layoutInflater.inflate(R.layout.list_item_option, parent, false))
    }

    override fun isForViewType(items: List<OptionItem>, position: Int): Boolean {
        return items[position] is OptionItem
    }

    override fun onBindViewHolder(
        items: List<OptionItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val viewHolder: OptionViewHolder = holder as OptionViewHolder
        val item: OptionItem? = items[position]

        viewHolder.optionTitle.text = item?.title
        viewHolder.optionIcon.setImageDrawable(item?.icon)
    }

    internal inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var optionTitle: TextView = itemView.findViewById(R.id.optionTitle)
        var optionIcon: ImageView = itemView.findViewById(R.id.optionIcon)

    }
}