package com.jsoft.pos.ui.views.tax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.ItemJoinVO
import com.jsoft.pos.data.model.TaxDao

class AssignItemTaxViewModel(application: Application) : AndroidViewModel(application) {

    val nameInput = MutableLiveData<String>()



    private val dao: TaxDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.taxDao()
    }

}