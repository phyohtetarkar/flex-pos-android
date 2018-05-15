package com.jsoft.es.ui.views.category

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.es.R
import com.jsoft.es.data.entity.CategoryVO
import com.jsoft.es.ui.custom.BindingViewHolder

class CategoryAdapter : ListAdapter<CategoryVO, BindingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_category, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position).apply { holder.bind(this) }
    }

    fun getItemAt(position: Int): CategoryVO {
        return getItem(position)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryVO>() {
            override fun areItemsTheSame(oldItem: CategoryVO, newItem: CategoryVO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CategoryVO, newItem: CategoryVO): Boolean {
                return oldItem == newItem
            }
        }
    }

}
