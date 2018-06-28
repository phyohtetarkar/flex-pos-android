package com.flex.pos.ui.views

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup

open class SimplePagedListAdapter<T>(
        DIFF_CALLBACK: DiffUtil.ItemCallback<T>,
        private val layoutId: Int
) : PagedListAdapter<T, BindingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val t = getItem(position)

        t?.apply { holder.bind(this as Any) }

    }

    fun getItemAt(position: Int): T {
        return super.getItem(position)!!
    }

}