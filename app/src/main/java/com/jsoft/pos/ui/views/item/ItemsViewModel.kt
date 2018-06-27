package com.jsoft.pos.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.model.ItemRepository
import com.jsoft.pos.data.model.ItemVOSearch
import com.jsoft.pos.data.utils.ObserverMutableLiveData
import com.jsoft.pos.ui.views.PagedListViewModel

class ItemsViewModel(application: Application) : AndroidViewModel(application), PagedListViewModel<ItemVO> {

    val searchModel = ObserverMutableLiveData<ItemVOSearch>()

    override val list: LiveData<PagedList<ItemVO>> = Transformations.switchMap(searchModel) {
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
