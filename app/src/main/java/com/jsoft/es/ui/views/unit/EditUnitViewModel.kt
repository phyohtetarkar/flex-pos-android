package com.jsoft.es.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.model.UnitDao
import com.jsoft.es.data.utils.DaoWorkerAsync

class EditUnitViewModel(application: Application) : AndroidViewModel(application) {

    val unit = ObservableField<Unit>()

    val unitInput = MutableLiveData<Int>()

    val unitLiveData: LiveData<Unit> =
            Transformations.switchMap(unitInput) { dao.findById(it) }

    private val dao: UnitDao

    init {
        val app = application as EasyShopApplication
        dao = app.db.unitDao()
    }

    fun save() {
        unit.get()?.apply {
            DaoWorkerAsync<Unit>({
                it.uniqueName = it.name.toUpperCase()
                if (it.id > 0) {
                    dao.update(it)
                } else {
                    dao.insert(it)
                }
            }, {

            }).execute(this)
        }
    }

}