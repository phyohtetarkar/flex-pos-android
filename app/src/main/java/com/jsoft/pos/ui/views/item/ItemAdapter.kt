package com.jsoft.pos.ui.views.item

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.ui.views.BindingViewHolder

class ItemAdapter : PagedListAdapter<ItemVO, BindingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_item, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    fun getItemAt(position: Int): ItemVO? {
        return super.getItem(position)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemVO>() {
            override fun areItemsTheSame(oldItem: ItemVO, newItem: ItemVO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemVO, newItem: ItemVO): Boolean {
                return oldItem == newItem
            }
        }
    }

}
