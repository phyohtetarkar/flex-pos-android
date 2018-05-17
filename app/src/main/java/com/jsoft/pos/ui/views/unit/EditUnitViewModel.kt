package com.jsoft.pos.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import com.jsoft.pos.FluentPosApplication
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditUnitViewModel(application: Application) : AndroidViewModel(application) {

    val unit = ObservableField<Unit>()

    val unitInput = MutableLiveData<Int>()

    val unitLiveData: LiveData<Unit> =
            Transformations.switchMap(unitInput) { dao.findById(it) }

    private val dao: UnitDao

    init {
        val app = application as FluentPosApplication
        dao = app.db.unitDao()
    }

    fun save() {
        unit.get()?.apply {
            DaoWorkerAsync<Unit>({
                dao.save(it)
            }, {

            }).execute(this)
        }
    }

}