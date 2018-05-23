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
import com.jsoft.pos.data.model.ItemService
import com.jsoft.pos.data.model.UnitDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    //val item = ObservableField<Item>()

    val itemInput = MutableLiveData<Long>()

    val item: LiveData<Item> = Transformations.switchMap(itemInput) {
        val live = MutableLiveData<Item>()

        DaoWorkerAsync<Long>({
            live.postValue(service.getItem(it))
        }, {

        }).execute(it)

        return@switchMap live
    }

    val categories: LiveData<List<Category>> by lazy { categoryDao.findAllCategories() }
    val units: LiveData<List<Unit>> by lazy { unitDao.findAllUnits() }

    private val service: ItemService
    private val categoryDao: CategoryDao
    private val unitDao: UnitDao

    init {
        val app = application as FluentPosApplication
        categoryDao = app.db.categoryDao()
        unitDao = app.db.unitDao()
        service = ItemService(app.db.itemDao(), unitDao, categoryDao)
    }

    fun save() {
        service.save(item.value)
    }

    fun delete() {
        service.delete(item.value)
    }

}