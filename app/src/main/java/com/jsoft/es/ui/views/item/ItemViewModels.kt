package com.jsoft.es.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import android.databinding.ObservableField
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.Item
import com.jsoft.es.data.entity.ItemVO
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.model.CategoryRepo
import com.jsoft.es.data.model.ItemRepo
import com.jsoft.es.data.model.ItemSearch
import com.jsoft.es.data.model.UnitRepo
import com.jsoft.es.data.utils.SearchMutableLiveData

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    val searchModel = SearchMutableLiveData<ItemSearch>()
    val items: LiveData<PagedList<ItemVO>> =
            Transformations.switchMap(searchModel) { repo.findItems(it) }

    private val repo: ItemRepo

    init {
        val app = application as EasyShopApplication
        repo = ItemRepo(app.db.itemDao())
    }

}

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    val item = ObservableField<Item>()

    val itemInput = MutableLiveData<Long>()
    val categoryInput = MutableLiveData<Int>()
    val unitInput = MutableLiveData<Int>()

    val itemLiveData: LiveData<Item> =
            Transformations.switchMap(itemInput) { repo.getItem(it) }

    val categoryLiveData: LiveData<Category> =
            Transformations.switchMap(categoryInput) { categoryRepo.getCategory(it) }

    val unitLiveData: LiveData<Unit> =
            Transformations.switchMap(unitInput) { unitRepo.getUnit(it) }

    private val repo: ItemRepo
    private val categoryRepo: CategoryRepo
    private val unitRepo: UnitRepo

    init {
        val app = application as EasyShopApplication
        repo = ItemRepo(app.db.itemDao())
        categoryRepo = CategoryRepo(app.db.categoryDao())
        unitRepo = UnitRepo(app.db.unitDao())
    }

    fun save() {
        item.get()?.apply { repo.save(this) }
    }

    fun delete() {
        item.get()?.apply { repo.delete(this) }
    }

}
