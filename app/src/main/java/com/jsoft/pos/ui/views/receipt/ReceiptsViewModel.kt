package com.jsoft.pos.ui.views.receipt

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.model.SaleRepository
import com.jsoft.pos.data.model.SaleSearch
import com.jsoft.pos.ui.views.PagedListViewModel

class ReceiptsViewModel(application: Application) : AndroidViewModel(application), PagedListViewModel<Sale> {

    val saleInput = MutableLiveData<SaleSearch>()

    override val list: LiveData<PagedList<Sale>> = Transformations.switchMap(saleInput) {
        return@switchMap repository.findSales(it)
    }

    private val repository: SaleRepository

    init {
        val app = application as FlexPosApplication
        repository = SaleRepository(app.db!!.saleDao())
    }

}