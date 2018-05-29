package com.jsoft.pos.ui.views.tax

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Tax
import com.jsoft.pos.data.model.TaxDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditTaxViewModel(app: FlexPosApplication) : AndroidViewModel(app) {

    val taxInput = MutableLiveData<Int>()

    val tax: LiveData<Tax> = Transformations.switchMap(taxInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Tax>()
        data.value = Tax()

        return@switchMap data
    }

    private val dao: TaxDao = app.db.taxDao()

    fun save() {
        tax.value?.apply {
            DaoWorkerAsync<Tax>({
                dao.save(it)
            }, {

            }).execute(this)
        }
    }

}