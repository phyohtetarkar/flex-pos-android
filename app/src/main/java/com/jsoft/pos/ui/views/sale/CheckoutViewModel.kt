package com.jsoft.pos.ui.views.sale

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.model.ItemRepository
import com.jsoft.pos.data.model.SaleDao
import com.jsoft.pos.data.model.SaleRepository
import com.jsoft.pos.data.utils.DaoWorkerAsync

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    val sale = MutableLiveData<Sale>()
    val saleItem = MutableLiveData<SaleItem>()
    val saleItems = MutableLiveData<List<SaleItem>>()

    private val saleDao: SaleDao

    private val repository: SaleRepository

    val saveObserver = MutableLiveData<Boolean>()

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
        DaoWorkerAsync<Sale>({
            saleDao.save(it, saleItems.value.orEmpty().toMutableList())
        }, {
            saveObserver.value = true
        }, {

        }).execute(sale.value)

    }

    fun updateSaleItem() {
        saleItem.value?.also {
            CheckOutItemsHolder.update(it)
        }
    }

    fun populateSale(list: List<SaleItem>) {
        repository.initializeSale(list, sale, saleItems)
    }

}