package com.jsoft.pos.ui.views.tax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.text.TextUtils
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Tax
import com.jsoft.pos.data.model.TaxDao
import com.jsoft.pos.data.utils.DaoWorkerAsync
import com.jsoft.pos.ui.utils.ValidatorUtils

class EditTaxViewModel(application: Application) : AndroidViewModel(application) {

    val nameValid = MutableLiveData<Boolean>()
    val valueValid = MutableLiveData<Boolean>()
    val nameNotEmpty = MutableLiveData<Boolean>()
    val nameUnique = MutableLiveData<Boolean>()
    val saveSuccess = MutableLiveData<Boolean>()
    val deleteSuccess = MutableLiveData<Boolean>()

    val taxInput = MutableLiveData<Int>()
    var checkedItemIds: Collection<Long>? = null

    val tax: LiveData<Tax> = Transformations.switchMap(taxInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Tax>()
        data.value = Tax()

        return@switchMap data
    }

    val assignBtnEnable = MutableLiveData<Boolean>()

    private val dao: TaxDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.taxDao()
    }

    fun save() {
        var hasErrors = false

        tax.value?.also {
            nameValid.value = !TextUtils.isEmpty(it.name)
            nameNotEmpty.value = nameValid.value

            if (nameValid.value == false) {
                hasErrors = true
            }
        }?.also {
            valueValid.value = ValidatorUtils.isValid(it.amount, ValidatorUtils.VALID_PERCENTAGE)
            if (valueValid.value == false) {
                hasErrors = true
            }
        }.takeUnless { hasErrors }?.also {
            DaoWorkerAsync<Tax>({
                val src = dao.findByUniqueNameSync(it.name)
                if (src != null && src.id != it.id) {
                    nameUnique.postValue(false)
                    nameValid.postValue(false)
                    return@DaoWorkerAsync false
                } else {
                    return@DaoWorkerAsync dao.save(it, checkedItemIds).let { true }
                }
            }, {
                saveSuccess.value = true
            }, {
                saveSuccess.value = false
            }).execute(it)
        }
    }

    fun delete() {
        DaoWorkerAsync<Tax>({
            dao.delete(it).let { true }
        }, {
            deleteSuccess.value = true
        }, {
            deleteSuccess.value = false
        }).execute(tax.value)
    }
}