package com.flex.pos.ui.views.category

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.db.SimpleSQLiteQuery
import com.flex.pos.FlexPosApplication
import com.flex.pos.data.entity.CategoryVO
import com.flex.pos.data.model.CategoryDao
import com.flex.pos.data.model.CategorySearch
import com.flex.pos.ui.views.SimpleListViewModel

class CategoriesViewModel(application: Application) : AndroidViewModel(application), SimpleListViewModel<CategoryVO> {

    val searchModel = MutableLiveData<CategorySearch>()

    override val list: LiveData<List<CategoryVO>> = Transformations.switchMap(searchModel) {
        dao.findCategories(SimpleSQLiteQuery(it.query, it.objects.toTypedArray()))
    }

    private val dao: CategoryDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.categoryDao()
    }

}
