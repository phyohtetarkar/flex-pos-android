package com.jsoft.pos.ui.views.discount

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.model.DiscountDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditDiscountViewModel(application: Application) : AndroidViewModel(application) {

    val discountInput = MutableLiveData<Int>()
    var checkedItemIds: Collection<Long>? = null

    val discount: LiveData<Discount> = Transformations.switchMap(discountInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Discount>()
        data.value = Discount()

        return@switchMap data
    }

    val assignBtnEnable = MutableLiveData<Boolean>()

    private val dao: DiscountDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.discountDao()
    }

    fun save() {
        DaoWorkerAsync<Discount>({
            dao.save(it, checkedItemIds)
        }, {

        }, {

        }).execute(discount.value)
    }

    fun updateDiscountMode(isPercent: Boolean) {
        discount.value?.apply {
            percentage = isPercent
        }
    }

}