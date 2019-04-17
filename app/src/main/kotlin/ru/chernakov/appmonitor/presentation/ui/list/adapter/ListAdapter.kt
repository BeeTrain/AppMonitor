package ru.chernakov.appmonitor.presentation.ui.list.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import ru.chernakov.appmonitor.data.dto.ApplicationDto

class ListAdapter(activity: FragmentActivity, items: ArrayList<ApplicationDto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var items: ArrayList<ApplicationDto> = items
    set(items) {
        field = items
        itemsSearch = items
        notifyDataSetChanged()
    }

    private var itemsSearch: ArrayList<ApplicationDto> = ArrayList()

    private val delegatesManager = AdapterDelegatesManager<ArrayList<ApplicationDto>>()

    init {
        itemsSearch = items
        delegatesManager.addDelegate(ListAdapterDelegate(activity))
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(viewGroup, position)
    }

    override fun getItemCount(): Int {
        return itemsSearch.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(itemsSearch, position, viewHolder)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val query = constraint.toString()
                if (query.isEmpty()) {
                    itemsSearch = items
                } else {
                    val itemsFiltered = ArrayList<ApplicationDto>()
                    for (ApplicationDto in items) {
                        if (ApplicationDto.name!!.toLowerCase().contains(query)
                            || ApplicationDto.apk!!.toLowerCase().contains(query)
                        ) {
                            itemsFiltered.add(ApplicationDto)
                        }
                        itemsSearch = itemsFiltered
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = itemsSearch

                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                itemsSearch = results.values as ArrayList<ApplicationDto>

                notifyDataSetChanged()
            }

        }
    }

    fun getItem(position: Int): ApplicationDto {
        return itemsSearch[position]
    }
}
