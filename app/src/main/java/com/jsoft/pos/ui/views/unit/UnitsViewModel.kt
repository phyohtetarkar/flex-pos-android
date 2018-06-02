package com.jsoft.pos.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.db.SimpleSQLiteQuery
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitDao
import com.jsoft.pos.data.model.UnitSearch
import com.jsoft.pos.data.utils.DaoWorkerAsync
import com.jsoft.pos.data.utils.SearchMutableLiveData
import com.jsoft.pos.ui.views.SimpleListViewModel

class UnitsViewModel(application: Application) : AndroidViewModel(application), SimpleListViewModel<Unit> {

    val searchModel = SearchMutableLiveData<UnitSearch>()

    val units: LiveData<List<Unit>> = Transformations.switchMap(searchModel) {
        dao.findUnits(SimpleSQLiteQuery(it.query, it.objects.toTypedArray()))
    }

    override val list: LiveData<List<Unit>> = Transformations.switchMap(searchModel) {
        dao.findUnits(SimpleSQLiteQuery(it.query, it.objects.toTypedArray()))
    }

    private val dao: UnitDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.unitDao()
    }

    fun delete(unit: Unit) {
        DaoWorkerAsync<Unit>({
            dao.delete(it)
        }, {

        }, {

        }).execute(unit)
    }

}
