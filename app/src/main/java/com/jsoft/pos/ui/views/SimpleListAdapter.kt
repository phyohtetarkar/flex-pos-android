package com.jsoft.pos.ui.views

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.Unit

open class SimpleListAdapter<T>(
        DIFF_CALLBACK: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BindingViewHolder>(DIFF_CALLBACK) {

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

        when (layoutId) {
            R.layout.layout_simple_list_item -> {
                if (t is Unit) {
                    holder.bind(t.name)
                } else if (t is Category) {
                    holder.bind(t.name)
                }
            }

            else -> holder.bind(t as Any)
        }
    }

    fun getItemAt(position: Int): T {
        return super.getItem(position)
    }

}
