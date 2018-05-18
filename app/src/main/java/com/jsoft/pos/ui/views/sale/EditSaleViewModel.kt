package com.jsoft.pos.ui.views.sale

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.jsoft.pos.FluentPosApplication
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.model.SaleDao

class EditSaleViewModel(application: Application) : AndroidViewModel(application) {

    val sale = ObservableField<Sale>()
    val saleItems = ObservableArrayList<SaleItem>()

    private val saleDao: SaleDao

    init {
        val app = application as FluentPosApplication
        saleDao = app.db.saleDao()
    }

    fun save() {

    }

}