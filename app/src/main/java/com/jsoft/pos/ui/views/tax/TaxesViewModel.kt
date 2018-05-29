package com.jsoft.pos.ui.views.tax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Tax
import com.jsoft.pos.data.model.TaxDao
import com.jsoft.pos.ui.views.SimpleListViewModel

class TaxesViewModel(application: Application) : AndroidViewModel(application), SimpleListViewModel<Tax> {

    override val list: LiveData<List<Tax>> by lazy { dao.findAllTaxes() }

    private val dao: TaxDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.taxDao()
    }

}