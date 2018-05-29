package com.jsoft.pos.ui.views.nav

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.model.ItemDao
import com.jsoft.pos.data.model.UnitDao

class ResourcesViewModel(application: Application) : AndroidViewModel(application) {

    val itemCount: LiveData<Long> by lazy { itemDao.findCount() }
    val unitCount: LiveData<Long> by lazy { unitDao.findCount() }
    val categoryCount: LiveData<Long> by lazy { categoryDao.findCount() }

    private val itemDao: ItemDao
    private val unitDao: UnitDao
    private val categoryDao: CategoryDao

    init {
        val app = application as FlexPosApplication
        itemDao = app.db.itemDao()
        unitDao = app.db.unitDao()
        categoryDao = app.db.categoryDao()
    }

}