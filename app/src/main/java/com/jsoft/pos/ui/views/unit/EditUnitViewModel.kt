package com.jsoft.pos.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FluentPosApplication
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditUnitViewModel(application: Application) : AndroidViewModel(application) {

    val unitInput = MutableLiveData<Int>()

    val unit: LiveData<Unit> = Transformations.switchMap(unitInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Unit>()
        data.value = Unit()

        return@switchMap data

    }

    private val dao: UnitDao

    init {
        val app = application as FluentPosApplication
        dao = app.db.unitDao()
    }

    fun save() {
        unit.value?.apply {
            DaoWorkerAsync<Unit>({
                dao.save(it)
            }, {

            }).execute(this)
        }
    }

}