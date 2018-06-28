package com.flex.pos.ui.views.sale

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.flex.pos.FlexPosApplication
import com.flex.pos.data.entity.Sale
import com.flex.pos.data.entity.SaleItem
import com.flex.pos.data.entity.TaxAmount
import com.flex.pos.data.model.ItemRepository
import com.flex.pos.data.model.SaleDao
import com.flex.pos.data.model.SaleRepository
import com.flex.pos.data.utils.DaoWorkerAsync

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    val saleId = MutableLiveData<Long>()
    val saleItems = MutableLiveData<List<SaleItem>>()

    val saleItem = MutableLiveData<SaleItem>()

    val vSaleItems = MutableLiveData<MutableList<SaleItem>>()
    val vTaxAmounts = MutableLiveData<List<TaxAmount>>()

    val sale = MediatorLiveData<Sale>()

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

        sale.addSource(saleId, {
            DaoWorkerAsync<Long>({
                return@DaoWorkerAsync sale.postValue(repository.getSaleSync(it)).let { true }
            }, {}, {}).execute(it)
        })
        sale.addSource(saleItems, {
            DaoWorkerAsync<List<SaleItem>>({
                return@DaoWorkerAsync sale.postValue(repository.initSale(it)).let { true }
            }, {}, {}).execute(it)
        })

    }

    fun save() {
        DaoWorkerAsync<Sale>({
            saleDao.save(it).let { true }
        }, {
            saveObserver.value = true
        }, {

        }).execute(sale.value)

    }

    fun updateSaleItem() {
        val iterator = vSaleItems.value?.listIterator()
        val mod = saleItem.value

        while (iterator?.hasNext() == true) {
            val origin = iterator.next()
            if (mod?.itemId == origin.itemId) {
                iterator.set(mod)
                break
            }
        }

        sale.value?.saleItems = vSaleItems.value.orEmpty().toMutableList()

    }

    fun removeSaleItem(saleItem: SaleItem) {
        vSaleItems.value?.remove(saleItem)
        sale.value?.saleItems = vSaleItems.value.orEmpty().toMutableList()
        vTaxAmounts.value = sale.value?.groupTaxes
    }

    fun removeAll() {
        vSaleItems.value?.clear()
        sale.value?.saleItems = mutableListOf()
        vTaxAmounts.value = sale.value?.groupTaxes
    }

}