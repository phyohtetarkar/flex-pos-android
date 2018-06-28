package com.flex.pos.ui.views.sale

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import com.flex.pos.FlexPosApplication
import com.flex.pos.data.entity.Category
import com.flex.pos.data.entity.ItemVO
import com.flex.pos.data.model.CategoryDao
import com.flex.pos.data.model.ItemRepository
import com.flex.pos.data.model.ItemVOSearch
import com.flex.pos.data.utils.ObserverMutableLiveData
import com.flex.pos.ui.views.PagedListViewModel

class SaleViewModel(application: Application): AndroidViewModel(application), PagedListViewModel<ItemVO> {

    val itemSearch = ObserverMutableLiveData<ItemVOSearch>()

    override val list: LiveData<PagedList<ItemVO>> = Transformations.switchMap(itemSearch) {
        repository.findItemVOs(it)
    }

    val categories: LiveData<List<Category>> by lazy { categoryDao.findAllCategories() }

    private val repository: ItemRepository
    private val categoryDao: CategoryDao

    init {
        val app = application as FlexPosApplication
        repository = ItemRepository(app.db.itemDao())
        categoryDao = app.db.categoryDao()
    }

}