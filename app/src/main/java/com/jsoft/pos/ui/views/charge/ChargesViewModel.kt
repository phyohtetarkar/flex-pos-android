package com.jsoft.pos.ui.views.charge

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Charge
import com.jsoft.pos.data.model.ChargeDao
import com.jsoft.pos.ui.views.SimpleListViewModel

class ChargesViewModel(application: Application) : AndroidViewModel(application), SimpleListViewModel<Charge> {

    override val list: LiveData<List<Charge>> by lazy { dao.findAllCharges() }

    private val dao: ChargeDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.chargeDao()
    }

}