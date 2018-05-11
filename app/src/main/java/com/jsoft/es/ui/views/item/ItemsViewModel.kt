package com.jsoft.es.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.db.SimpleSQLiteQuery
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.ItemVO
import com.jsoft.es.data.model.ItemDao
import com.jsoft.es.data.model.ItemSearch
import com.jsoft.es.data.utils.SearchMutableLiveData

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    val searchModel = SearchMutableLiveData<ItemSearch>()

    val items: LiveData<PagedList<ItemVO>> = Transformations.switchMap(searchModel) {
        LivePagedListBuilder(dao.findItems(SimpleSQLiteQuery(it.query, it.objects.toTypedArray())), 20).build()
    }

    private val dao: ItemDao

    init {
        val app = application as EasyShopApplication
        dao = app.db.itemDao()
    }

}
