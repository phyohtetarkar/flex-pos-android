package com.jsoft.pos.ui.views

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.jsoft.pos.BR

class BindingViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(obj: Any) {

        binding.setVariable(BR.obj, obj)
        binding.executePendingBindings()
    }

}
