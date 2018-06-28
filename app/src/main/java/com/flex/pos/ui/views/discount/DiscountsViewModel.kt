package com.flex.pos.ui.views.discount

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.flex.pos.FlexPosApplication
import com.flex.pos.data.entity.Discount
import com.flex.pos.data.model.DiscountDao
import com.flex.pos.ui.views.SimpleListViewModel

class DiscountsViewModel(application: Application) : AndroidViewModel(application), SimpleListViewModel<Discount> {

    override val list: LiveData<List<Discount>> by lazy { dao.findAllDiscounts() }

    private val dao: DiscountDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.discountDao()
    }

}