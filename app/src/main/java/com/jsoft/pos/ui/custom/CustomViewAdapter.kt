package com.jsoft.pos.ui.custom

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsoft.pos.BR

abstract class CustomViewAdapter<T>(
        private var parent: ViewGroup?,
        private var itemViewId: Int
) {

    protected val list = ObservableArrayList<T>()

    protected abstract fun onBindView(holder: SimpleViewHolder, position: Int)

    open fun onCreateView(parent: ViewGroup?): SimpleViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, itemViewId, parent, false)
        return SimpleViewHolder(binding)
    }

    fun submitList(data: List<T>?) {
        data?.apply {
            list.clear()
            list.addAll(this)
            notifyChange()
        }
    }

    fun destroyView() {
        parent = null
    }

    private fun notifyChange() {
        for (i in 0 until list.size) {
            val holder = onCreateView(parent)
            onBindView(holder, i)
            parent?.addView(holder.itemView)
        }
    }

    class SimpleViewHolder(val itemView: View) {

        private lateinit var binding: ViewDataBinding

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(obj: Any) {
            binding.setVariable(BR.obj, obj)
            binding.executePendingBindings()
        }

    }

}