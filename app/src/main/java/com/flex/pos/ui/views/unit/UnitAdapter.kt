package com.flex.pos.ui.views.unit

import android.support.v7.util.DiffUtil
import com.flex.pos.data.entity.Unit
import com.flex.pos.ui.views.SimpleListAdapter

class UnitAdapter : SimpleListAdapter<Unit>(callback) {

    companion object {
        private val callback = object : DiffUtil.ItemCallback<Unit>() {
            override fun areItemsTheSame(oldItem: Unit?, newItem: Unit?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Unit?, newItem: Unit?): Boolean {
                return oldItem == newItem
            }

        }
    }

}