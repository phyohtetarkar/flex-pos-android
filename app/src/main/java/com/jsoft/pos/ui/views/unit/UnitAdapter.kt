package com.jsoft.pos.ui.views.unit

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.pos.R
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.ui.custom.BindingViewHolder

class UnitAdapter : ListAdapter<Unit, BindingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_unit, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position).apply { holder.bind(this) }
    }

    fun getItemAt(position: Int): Unit {
        return super.getItem(position)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Unit>() {
            override fun areItemsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Unit, newItem: Unit): Boolean {
                return oldItem == newItem
            }
        }
    }

}
