package com.jsoft.pos.ui.views.discount

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.model.DiscountDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditDiscountViewModel(app: FlexPosApplication) : AndroidViewModel(app) {

    val discountInput = MutableLiveData<Int>()

    val tax: LiveData<Discount> = Transformations.switchMap(discountInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Discount>()
        data.value = Discount()

        return@switchMap data
    }

    private val dao: DiscountDao = app.db.discountDao()

    fun save() {
        tax.value?.apply {
            DaoWorkerAsync<Discount>({
                dao.save(it)
            }, {

            }).execute(this)
        }
    }

}