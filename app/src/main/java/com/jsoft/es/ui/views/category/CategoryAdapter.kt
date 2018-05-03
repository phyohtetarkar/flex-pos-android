package com.jsoft.es.ui.views.category

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.es.R
import com.jsoft.es.data.entity.CategoryVO
import com.jsoft.es.ui.custom.BindingViewHolder

class CategoryAdapter : RecyclerView.Adapter<BindingViewHolder>() {

    private var list: MutableList<CategoryVO> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_category, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val c = list[position] as CategoryVO?
        if (c != null) {
            holder.bind(c)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItemAt(position: Int): CategoryVO {
        return list[position]
    }

    fun refreshItemAt(position: Int) {
        notifyItemChanged(position)
    }

    fun setData(list: MutableList<CategoryVO>) {
        this.list = list
        notifyDataSetChanged()
    }

}
