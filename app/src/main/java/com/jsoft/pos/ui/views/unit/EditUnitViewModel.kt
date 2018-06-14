package com.jsoft.pos.ui.views.unit

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.UnitDao
import com.jsoft.pos.data.utils.DaoWorkerAsync
import com.jsoft.pos.ui.utils.ValidatorUtils

class EditUnitViewModel(application: Application) : AndroidViewModel(application) {

    val nameValid = MutableLiveData<Boolean>()
    val nameConflict = MutableLiveData<Boolean>()
    val saveSuccess = MutableLiveData<Boolean>()

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
        val app = application as FlexPosApplication
        dao = app.db.unitDao()
    }

    fun save() {

        var hasErrors = false

        unit.value?.also {
            if (!ValidatorUtils.isValid(it.name, ValidatorUtils.NOT_EMPTY)) {
                nameValid.value = false
                hasErrors = true
            }
        }.takeUnless { hasErrors }?.let {
            DaoWorkerAsync<Unit>({
                if (dao.findByUniqueNameSync(it.name.toUpperCase()) != null) {
                    nameConflict.value = true
                    return@DaoWorkerAsync false
                } else {
                    return@DaoWorkerAsync dao.save(it).let { true }
                }
            }, {
                saveSuccess.value = true
            }, {
                saveSuccess.value = false
            }).execute(it)
        }


    }

}