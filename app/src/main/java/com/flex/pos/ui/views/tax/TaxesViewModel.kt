package com.flex.pos.ui.views.tax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.flex.pos.FlexPosApplication
import com.flex.pos.data.entity.Tax
import com.flex.pos.data.model.TaxDao
import com.flex.pos.ui.views.SimpleListViewModel

class TaxesViewModel(application: Application) : AndroidViewModel(application), SimpleListViewModel<Tax> {

    override val list: LiveData<List<Tax>> by lazy { dao.findAllTaxes() }

    private val dao: TaxDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.taxDao()
    }

}