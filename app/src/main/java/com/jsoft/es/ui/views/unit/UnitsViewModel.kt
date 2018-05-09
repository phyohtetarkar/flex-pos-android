package com.jsoft.es.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.model.UnitRepo
import com.jsoft.es.data.model.UnitSearch
import com.jsoft.es.data.utils.SearchMutableLiveData

class UnitsViewModel(application: Application) : AndroidViewModel(application) {

    val searchModel = SearchMutableLiveData<UnitSearch>()

    val units: LiveData<List<Unit>> =
            Transformations.switchMap(searchModel) { repo.findUnits(it) }

    private val repo: UnitRepo

    init {
        val app = application as EasyShopApplication
        repo = UnitRepo(app.db.unitDao())
    }

    fun delete(unit: Unit) {
        repo.delete(unit)
    }

}
