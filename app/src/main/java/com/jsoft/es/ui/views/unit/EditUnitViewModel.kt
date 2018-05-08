package com.jsoft.es.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.model.UnitRepo

class EditUnitViewModel(application: Application) : AndroidViewModel(application) {

    val unit = ObservableField<Unit>()

    val unitInput = MutableLiveData<Int>()

    val unitLiveData: LiveData<Unit> =
            Transformations.switchMap(unitInput) { repo.getUnit(it) }

    private val repo: UnitRepo

    init {
        val app = application as EasyShopApplication
        repo = UnitRepo(app.db.unitDao())
    }

    fun save() {
        unit.get()?.apply { repo.save(this) }
    }

}