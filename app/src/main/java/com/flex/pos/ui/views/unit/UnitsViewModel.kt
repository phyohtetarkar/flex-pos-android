package com.flex.pos.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.db.SimpleSQLiteQuery
import com.flex.pos.FlexPosApplication
import com.flex.pos.data.entity.Unit
import com.flex.pos.data.model.UnitDao
import com.flex.pos.data.model.UnitSearch
import com.flex.pos.data.utils.DaoWorkerAsync
import com.flex.pos.ui.views.SimpleListViewModel

class UnitsViewModel(application: Application) : AndroidViewModel(application), SimpleListViewModel<Unit> {

    val deleteSuccess = MutableLiveData<Boolean>()

    val searchModel = MutableLiveData<UnitSearch>()

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
            dao.delete(it).let { true }
        }, {
            deleteSuccess.value = true
        }, {
            deleteSuccess.value = false
        }).execute(unit)
    }

}
