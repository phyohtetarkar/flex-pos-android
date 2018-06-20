package com.jsoft.pos.ui.views.discount

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.text.TextUtils
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.model.DiscountDao
import com.jsoft.pos.data.utils.DaoWorkerAsync
import com.jsoft.pos.ui.utils.ValidatorUtils

class EditDiscountViewModel(application: Application) : AndroidViewModel(application) {

    val nameValid = MutableLiveData<Boolean>()
    val valueValid = MutableLiveData<Boolean>()
    val nameNotEmpty = MutableLiveData<Boolean>()
    val nameUnique = MutableLiveData<Boolean>()
    val saveSuccess = MutableLiveData<Boolean>()
    val deleteSuccess = MutableLiveData<Boolean>()

    val discountInput = MutableLiveData<Int>()
    var checkedItemIds: Collection<Long>? = null

    val discount: LiveData<Discount> = Transformations.switchMap(discountInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Discount>()
        data.value = Discount()

        return@switchMap data
    }

    val assignBtnEnable = MutableLiveData<Boolean>()

    private val dao: DiscountDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.discountDao()
    }

    fun save() {
        var hasErrors = false

        discount.value?.also {
            nameValid.value = !TextUtils.isEmpty(it.name)
            nameNotEmpty.value = nameValid.value

            if (nameValid.value == false) {
                hasErrors = true
            }
        }?.also {
            if (it.percentage) {
                valueValid.value = ValidatorUtils.isValid(it.amount, ValidatorUtils.VALID_PERCENTAGE)
            }

            if (valueValid.value == false) {
                hasErrors = true
            }
        }.takeUnless { hasErrors }?.let {
            DaoWorkerAsync<Discount>({
                if (dao.findByUniqueNameSync(it.name) != null) {
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
        DaoWorkerAsync<Discount>({
            dao.delete(it).let { true }
        }, {
            deleteSuccess.value = true
        }, {
            deleteSuccess.value = false
        }).execute(discount.value)
    }

    fun updateDiscountMode(isPercent: Boolean) {
        discount.value?.apply {
            percentage = isPercent
        }
    }

}