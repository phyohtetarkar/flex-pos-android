package com.jsoft.pos.ui.views.receipt

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.model.ItemRepository
import com.jsoft.pos.data.model.SaleDao
import com.jsoft.pos.data.model.SaleRepository
import com.jsoft.pos.data.utils.DaoWorkerAsync

class ReceiptDetailViewModel(application: Application) : AndroidViewModel(application) {

    val saleId = MutableLiveData<Long>()

    val sale: LiveData<Sale> = Transformations.switchMap(saleId) {
        repository.getSale(it)
    }

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

    fun update() {
        DaoWorkerAsync<Sale>({
            return@DaoWorkerAsync saleDao.update(it).let { true }
        }, {}, {}).execute(sale.value)
    }
}