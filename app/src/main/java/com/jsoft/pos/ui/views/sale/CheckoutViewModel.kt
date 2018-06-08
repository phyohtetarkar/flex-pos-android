package com.jsoft.pos.ui.views.sale

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.model.ItemRepository
import com.jsoft.pos.data.model.SaleDao
import com.jsoft.pos.data.model.SaleRepository

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    val sale = MutableLiveData<Sale>()
    val saleItem = MutableLiveData<SaleItem>()
    val saleItems = MutableLiveData<List<SaleItem>>()

    private val saleDao: SaleDao

    private val repository: SaleRepository

    init {
        val app = application as FlexPosApplication
        saleDao = app.db.saleDao()
        repository = SaleRepository(saleDao,
                ItemRepository(
                        app.db.itemDao(),
                        app.db.unitDao(),
                        app.db.categoryDao(),
                        app.db.taxDao(),
                        app.db.discountDao())
        )
    }

    fun save() {

    }

    fun updateSaleItem() {
        saleItem.value?.also {
            CheckOutItemsHolder.update(it)
        }
    }

    fun populateSale(list: List<SaleItem>) {
        repository.createSale(list, sale, saleItems)
    }

    fun plusQty() {
        val si = saleItem.value
        si?.quantity = si?.quantity?.inc() ?: 0
        saleItem.value = si
    }

    fun minusQty() {
        val si = saleItem.value

        val qty = si?.quantity?.minus(1) ?: 0

        if (qty > 0) {
            si?.quantity = qty
            saleItem.value = si
        }

    }

}