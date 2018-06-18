package com.jsoft.pos.ui.views.item

import android.support.v7.util.DiffUtil
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.ui.views.SimplePagedListAdapter

class ItemVOAdapter(layoutRes: Int) : SimplePagedListAdapter<ItemVO>(callback, layoutRes) {

    companion object {
        private val callback = object : DiffUtil.ItemCallback<ItemVO>() {
            override fun areItemsTheSame(oldItem: ItemVO?, newItem: ItemVO?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: ItemVO?, newItem: ItemVO?): Boolean {
                return oldItem == newItem
            }

        }
    }

}