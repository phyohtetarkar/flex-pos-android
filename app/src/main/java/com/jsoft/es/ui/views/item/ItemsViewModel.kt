package com.jsoft.es.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.ItemVO
import com.jsoft.es.data.model.ItemRepo
import com.jsoft.es.data.model.ItemSearch
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
