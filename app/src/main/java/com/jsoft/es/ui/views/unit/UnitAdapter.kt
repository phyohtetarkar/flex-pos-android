package com.jsoft.es.ui.views.unit

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.es.R
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.ui.custom.BindingViewHolder

class UnitAdapter : RecyclerView.Adapter<BindingViewHolder>() {

    private var list: MutableList<Unit> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_unit, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val u = list[position] as Unit?
        if (u != null) {
            holder.bind(u)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItemAt(position: Int): Unit {
        return list[position]
    }

    fun refreshItemAt(position: Int) {
        notifyItemChanged(position)
    }

    fun setData(list: MutableList<Unit>) {
        this.list = list
        notifyDataSetChanged()
    }

}
