package com.jsoft.es.ui.views.category

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.db.SimpleSQLiteQuery
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.CategoryVO
import com.jsoft.es.data.model.CategoryDao
import com.jsoft.es.data.model.CategorySearch
import com.jsoft.es.data.utils.SearchMutableLiveData

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    val searchModel = SearchMutableLiveData<CategorySearch>()

    val categories: LiveData<List<CategoryVO>> = Transformations.switchMap(searchModel) {
        dao.findCategories(SimpleSQLiteQuery(it.query, it.objects.toTypedArray()))
    }

    private val dao: CategoryDao

    init {
        val app = application as EasyShopApplication
        dao = app.db.categoryDao()
    }

}
