package com.jsoft.pos.ui.views.category

import android.support.v7.util.DiffUtil
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.ui.views.SimpleListAdapter

class CategoryAdapter : SimpleListAdapter<Category>(callback) {

    companion object {
        private val callback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category?, newItem: Category?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Category?, newItem: Category?): Boolean {
                return oldItem == newItem
            }

        }
    }
}