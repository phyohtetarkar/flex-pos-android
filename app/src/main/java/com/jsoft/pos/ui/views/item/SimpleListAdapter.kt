package com.jsoft.pos.ui.views.item

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.Unit

class SimpleListAdapter<T> : RecyclerView.Adapter<SimpleListAdapter.SimpleListViewHolder>() {

    private var list: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_simple_list_item, parent, false)
        return SimpleListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleListViewHolder, position: Int) {
        val t = list[position]
        val textView = holder.itemView.findViewById(R.id.simple_list_item) as TextView

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
