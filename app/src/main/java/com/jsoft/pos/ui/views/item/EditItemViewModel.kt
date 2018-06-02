package com.jsoft.pos.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.Item
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.model.ItemRepository
import com.jsoft.pos.data.model.UnitDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    //val item = ObservableField<Item>()

    val itemInput = MutableLiveData<Long>()

    val item: LiveData<Item> = Transformations.switchMap(itemInput) {
        val liveItem = MutableLiveData<Item>()

        DaoWorkerAsync<Long>({
            liveItem.postValue(repository.getItem(it))
        },{},{}).execute(it)

        return@switchMap liveItem
    }

    val categories: LiveData<List<Category>> by lazy { categoryDao.findAllCategories() }
    val units: LiveData<List<Unit>> by lazy { unitDao.findAllUnits() }

    private val repository: ItemRepository
    private val categoryDao: CategoryDao
    private val unitDao: UnitDao

    init {
        val app = application as FlexPosApplication
        categoryDao = app.db.categoryDao()
        unitDao = app.db.unitDao()
        repository = ItemRepository(app.db.itemDao(), unitDao, categoryDao)
    }

    fun save() {
        DaoWorkerAsync<Item>({
            repository.save(it)
        },{

        },{

        }).execute(item.value)
    }

    fun delete() {
        DaoWorkerAsync<Item>({
            repository.delete(it)
        },{

        },{

        }).execute(item.value)
    }

}