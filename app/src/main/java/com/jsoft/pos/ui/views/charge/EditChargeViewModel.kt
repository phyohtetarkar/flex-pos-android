package com.jsoft.pos.ui.views.charge

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Charge
import com.jsoft.pos.data.model.ChargeDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditChargeViewModel(application: Application) : AndroidViewModel(application) {

    val chargeInput = MutableLiveData<Int>()
    var checkedItemIds: Collection<Long>? = null

    val charge: LiveData<Charge> = Transformations.switchMap(chargeInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Charge>()
        data.value = Charge()

        return@switchMap data
    }

    val assignBtnEnable = MutableLiveData<Boolean>()

    private val dao: ChargeDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.chargeDao()
    }

    fun save() {
        DaoWorkerAsync<Charge>({
            dao.save(it, checkedItemIds)
        }, {

        }, {

        }).execute(charge.value)
    }

    fun updateChargeMode(isPercent: Boolean) {
        charge.value?.apply {
            percentage = isPercent
        }
    }
}