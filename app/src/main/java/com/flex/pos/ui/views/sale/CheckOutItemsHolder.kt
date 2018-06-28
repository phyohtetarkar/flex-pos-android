package com.flex.pos.ui.views.sale

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.flex.pos.data.entity.ItemVO
import com.flex.pos.data.entity.SaleItem

object CheckOutItemsHolder {

    private val saleItems = ObservableArrayList<SaleItem>()

    val itemCount: Int
        get() = saleItems.map { it.quantity }.sum()

    val list: List<SaleItem>
        get() = saleItems.toList()

    val onSale: Boolean
        get() = saleItems.isNotEmpty()

    fun add(vo: ItemVO) {
        val saleItem = SaleItem(quantity = 1, price = vo.price, itemId = vo.id)

        val result = saleItems.find { it.itemId == vo.id }

        if (result != null) {
            val index = saleItems.indexOf(result)
            result.quantity += 1
            saleItems.remove(result)
            saleItems.add(index, result)
        } else {
            saleItems.add(saleItem)
        }

    }

    fun remove(saleItem: SaleItem) {
        saleItems.find { it.itemId == saleItem.itemId }?.also {
            saleItems.remove(it)
        }
    }

    fun update(saleItem: SaleItem?) {
        saleItem?.also { si ->
            val result = saleItems.find { it.itemId == si.itemId }
            result?.quantity = si.quantity
            result?.remark = si.remark
            result?.notifyChange()
        }
    }

    fun clear() {
        saleItems.clear()
    }

    fun addOnListChangeListener(listener: ObservableList.OnListChangedCallback<ObservableList<SaleItem>>) {
        saleItems.addOnListChangedCallback(listener)
    }

    fun removeOnListChangeListener(listener: ObservableList.OnListChangedCallback<ObservableList<SaleItem>>) {
        saleItems.removeOnListChangedCallback(listener)
    }

}