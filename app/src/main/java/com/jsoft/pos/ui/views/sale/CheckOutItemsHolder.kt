package com.jsoft.pos.ui.views.sale

import android.databinding.ObservableArrayMap
import android.databinding.ObservableMap
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.entity.SaleItem

object CheckOutItemsHolder {

    private val saleItems = ObservableArrayMap<Long, SaleItem>()

    val itemCount: Int
        get() = saleItems.values.map { it.quantity }.sum()

    val list: List<SaleItem>
        get() = saleItems.values.toList()

    val onSale: Boolean
        get() = saleItems.values.isNotEmpty()

    fun add(vo: ItemVO) {
        val saleItem = SaleItem(quantity = 1, price = vo.price, itemId = vo.id)

        val result = saleItems[saleItem.itemId]

        if (result != null) {
            result.quantity += 1
            saleItems[saleItem.itemId] = result
        } else {
            saleItems[saleItem.itemId] = saleItem
        }
    }

    fun remove(saleItem: SaleItem) {
        saleItems.remove(saleItem.itemId)
    }

    fun update(saleItem: SaleItem?) {
        saleItem?.also {
            saleItems[it.itemId] = it
        }
    }

    fun clear() {
        saleItems.clear()
    }

    fun addOnMapChangeListener(listener: ObservableMap.OnMapChangedCallback<ObservableArrayMap<Long, SaleItem>, Long, SaleItem>) {
        saleItems.addOnMapChangedCallback(listener)
    }

    fun removeOnMapChangeListener(listener: ObservableMap.OnMapChangedCallback<ObservableArrayMap<Long, SaleItem>, Long, SaleItem>) {
        saleItems.removeOnMapChangedCallback(listener)
    }

}