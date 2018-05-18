package com.jsoft.pos.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FluentPosApplication
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.Item
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.model.ItemDao
import com.jsoft.pos.data.model.UnitDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    //val item = ObservableField<Item>()

    val itemInput = MutableLiveData<Long>()

    val item: LiveData<Item> = Transformations.switchMap(itemInput) {
        val live = MutableLiveData<Item>()

        if (it > 0) {
            DaoWorkerAsync<Long>({
                val item = dao.findByIdSync(it)
                item.category = categoryDao.findByIdSync(item.categoryId)
                item.unit = unitDao.findByIdSync(item.unitId)
                live.postValue(item)

            }, {

            }).execute(it)
        } else {
            live.value = Item()
        }

        return@switchMap live
    }

    val categories: LiveData<List<Category>> by lazy { categoryDao.findAllCategories() }
    val units: LiveData<List<Unit>> by lazy { unitDao.findAllUnits() }

    private val dao: ItemDao
    private val categoryDao: CategoryDao
    private val unitDao: UnitDao

    init {
        val app = application as FluentPosApplication
        dao = app.db.itemDao()
        categoryDao = app.db.categoryDao()
        unitDao = app.db.unitDao()
    }

    fun save() {
        item.value?.apply {
            DaoWorkerAsync<Item>({
                dao.save(it)
            }, {

            }).execute(this)
        }
    }

    fun delete() {
        item.value?.apply {
            DaoWorkerAsync<Item>({
                dao.delete(it)
            }, {

            }).execute(this)
        }
    }

}