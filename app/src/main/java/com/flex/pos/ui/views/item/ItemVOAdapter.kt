package com.flex.pos.ui.views.item

import android.support.v7.util.DiffUtil
import com.flex.pos.data.entity.ItemVO
import com.flex.pos.ui.views.SimplePagedListAdapter

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