package com.jsoft.pos.ui.views.category

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.db.SimpleSQLiteQuery
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.CategoryVO
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.model.CategorySearch
import com.jsoft.pos.data.utils.SearchMutableLiveData
import com.jsoft.pos.ui.views.SimpleListViewModel

class CategoriesViewModel(app: FlexPosApplication) : AndroidViewModel(app), SimpleListViewModel<CategoryVO> {

    val searchModel = SearchMutableLiveData<CategorySearch>()

    override val list: LiveData<List<CategoryVO>> = Transformations.switchMap(searchModel) {
        dao.findCategories(SimpleSQLiteQuery(it.query, it.objects.toTypedArray()))
    }

    private val dao: CategoryDao = app.db.categoryDao()

}
