package com.jsoft.es.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.Item
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.model.CategoryDao
import com.jsoft.es.data.model.ItemDao
import com.jsoft.es.data.model.UnitDao
import com.jsoft.es.data.utils.DaoWorkerAsync

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    val item = ObservableField<Item>()

    val itemInput = MutableLiveData<Long>()
    val categoryInput = MutableLiveData<Int>()

    val itemLiveData: LiveData<Item> =
            Transformations.switchMap(itemInput) { dao.findById(it) }

    val categoryLiveData: LiveData<Category> =
            Transformations.switchMap(categoryInput) { categoryDao.findById(it) }

    val categories: LiveData<List<Category>> by lazy { categoryDao.findAllCategories() }
    val units: LiveData<List<Unit>> by lazy { unitDao.findAllUnits() }

    private val dao: ItemDao
    private val categoryDao: CategoryDao
    private val unitDao: UnitDao

    init {
        val app = application as EasyShopApplication

        dao = app.db.itemDao()
        categoryDao = app.db.categoryDao()
        unitDao = app.db.unitDao()
    }

    fun save() {
        item.get()?.apply {
            DaoWorkerAsync<Item>({
                dao.save(it, mutableListOf())
            }, {

            }).execute(this)
        }
    }

    fun delete() {
        item.get()?.apply {
            DaoWorkerAsync<Item>({
                dao.delete(it)
            }, {

            }).execute(this)
        }
    }

}