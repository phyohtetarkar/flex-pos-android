package com.jsoft.es.ui.views.item

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.Unit

class SimpleListAdapter<T> : RecyclerView.Adapter<SimpleListAdapter.SimpleListViewHolder>() {

    private var list: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return SimpleListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleListViewHolder, position: Int) {
        val t = list[position]
        val textView = holder.itemView.findViewById(android.R.id.text1) as TextView

        when (t) {
            is Category -> textView.text = t.name
            is Unit -> textView.text = t.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItemAt(position: Int): T {
        return list[position]
    }

    fun setData(list: MutableList<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    class SimpleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
