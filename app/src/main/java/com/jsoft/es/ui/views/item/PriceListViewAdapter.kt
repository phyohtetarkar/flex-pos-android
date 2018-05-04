package com.jsoft.es.ui.views.item

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jsoft.es.BR
import com.jsoft.es.R
import com.jsoft.es.data.entity.ItemPrice
import com.jsoft.es.ui.custom.SimpleViewAdapter

class PriceListViewAdapter(parent: ViewGroup) : SimpleViewAdapter<PriceListViewAdapter.PriceViewHolder>(parent) {

    var list: MutableList<ItemPrice> = mutableListOf()

    override fun onCreateView(parent: ViewGroup): PriceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_item_price, parent, false)
        return PriceViewHolder(binding)
    }

    override fun onBindView(holder: PriceViewHolder, position: Int) {
        list.getOrNull(position)?.apply {
            holder.bind(this)
        }

    }

    override fun getViewCount(): Int {
        return list.size
    }

    fun setPriceList(list: MutableList<ItemPrice>) {
        this.list = list
        notifyChange()
    }

    fun addPrice(itemPrice: ItemPrice) {
        val position = list.size
        list.add(position, itemPrice)
        notifyInserted(position)
    }

    fun deletePrice(position: Int) {
        list.removeAt(position)
        notifyDelete(position)
    }

    class PriceViewHolder(private val binding: ViewDataBinding) : SimpleViewHolder(binding) {

        fun bind(obj: Any?) {
            binding.setVariable(BR.itemPrice, obj)
            binding.executePendingBindings()
        }

    }

}