package com.flex.pos.ui.views

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.flex.pos.BR

class BindingViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(obj: Any) {
        binding.setVariable(BR.obj, obj)
        binding.executePendingBindings()
    }

    fun bind(variableId: Int, obj: Any) {
        binding.setVariable(variableId, obj)
        binding.executePendingBindings()
    }

}
