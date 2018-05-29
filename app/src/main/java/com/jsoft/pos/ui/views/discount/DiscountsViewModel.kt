package com.jsoft.pos.ui.views.discount

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.model.DiscountDao
import com.jsoft.pos.ui.views.SimpleListViewModel

class DiscountsViewModel(app: FlexPosApplication) : AndroidViewModel(app), SimpleListViewModel<Discount> {

    override val list: LiveData<List<Discount>> by lazy { dao.findAllDiscounts() }

    private val dao: DiscountDao = app.db.discountDao()

}