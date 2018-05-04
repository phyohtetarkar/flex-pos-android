package com.jsoft.es.ui.custom

import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup

abstract class SimpleViewAdapter<VH: SimpleViewAdapter.SimpleViewHolder>(private val parent: ViewGroup) {

    protected abstract fun onCreateView(parent: ViewGroup): VH
    protected abstract fun onBindView(holder: VH, position: Int)
    protected abstract fun getViewCount(): Int

    protected fun notifyChange() {
        for (i in 0..getViewCount()) {
            val holder = onCreateView(parent)
            onBindView(holder, i)
            parent.addView(holder.itemView)
        }
    }

    protected fun notifyInserted(position: Int) {
        val holder = onCreateView(parent)
        onBindView(holder, position)
        parent.addView(holder.itemView, position)
    }

    protected fun notifyDelete(position: Int) {
        parent.removeViewAt(position)
    }

    open class SimpleViewHolder(val itemView: View) {

        private lateinit var binding: ViewDataBinding

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
        }

    }

}