package com.jsoft.pos.ui.views.tax

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Tax
import com.jsoft.pos.data.model.TaxDao
import com.jsoft.pos.ui.views.SimpleListViewModel

class TaxesViewModel(app: FlexPosApplication) : AndroidViewModel(app), SimpleListViewModel<Tax> {

    override val list: LiveData<List<Tax>> by lazy { dao.findAllTaxes() }

    private val dao: TaxDao = app.db.taxDao()

}