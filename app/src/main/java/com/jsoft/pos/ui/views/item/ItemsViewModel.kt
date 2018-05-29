package com.jsoft.pos.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.db.SimpleSQLiteQuery
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.model.ItemDao
import com.jsoft.pos.data.model.ItemSearch
import com.jsoft.pos.data.utils.SearchMutableLiveData
import com.jsoft.pos.ui.views.PagedListViewModel

class ItemsViewModel(application: Application) : AndroidViewModel(application), PagedListViewModel<ItemVO> {

    val searchModel = SearchMutableLiveData<ItemSearch>()

    override val list: LiveData<PagedList<ItemVO>> = Transformations.switchMap(searchModel) {
        LivePagedListBuilder(dao.findItems(SimpleSQLiteQuery(it.query, it.objects.toTypedArray())), 20).build()
    }

    private val dao: ItemDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.itemDao()
    }

}
