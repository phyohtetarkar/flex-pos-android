package com.jsoft.pos.ui.views

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.pos.BR
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.*
import com.jsoft.pos.data.entity.Unit

open class SimpleListAdapter<T>(DIFF_CALLBACK: DiffUtil.ItemCallback<T>) : ListAdapter<T, BindingViewHolder>(DIFF_CALLBACK) {

    private var layoutId = R.layout.layout_simple_list_item

    constructor(DIFF_CALLBACK: DiffUtil.ItemCallback<T>, layoutId: Int) : this(DIFF_CALLBACK) {
        this.layoutId = layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val t = getItemAt(position)

        when (t) {
            is Unit -> holder.bind(t.name)
            is Category -> holder.bind(t.name)

            is Tax -> {
                holder.bind(BR.first, t.name)
                holder.bind(BR.second, t.taxDesc)
            }

            is Discount -> {
                holder.bind(BR.first, t.name)
                holder.bind(BR.second, t.discountDesc)
            }

            else -> holder.bind(t as Any)
        }
    }

    fun getItemAt(position: Int): T {
        return super.getItem(position)
    }

    fun toggleCheck(isChecked: Boolean) {
        for (i in 0..itemCount) {
            val t = getItemAt(i)
            if (t is Checkable) {
                t._checked = isChecked
            }
        }
        notifyDataSetChanged()
    }

    fun getCheckedItemIds(): MutableList<Long> {
        val list = mutableListOf<Long>()
        for (i in 0..itemCount) {
            val t = getItemAt(i)
            if (t is Item && t._checked) {
                list.add(t.id)
            }
        }

        return list
    }

}
