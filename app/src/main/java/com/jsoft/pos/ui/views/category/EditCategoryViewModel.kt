package com.jsoft.pos.ui.views.category

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.utils.DaoWorkerAsync
import com.jsoft.pos.ui.utils.ValidatorUtils

class EditCategoryViewModel(application: Application) : AndroidViewModel(application) {

    val nameValid = MutableLiveData<Boolean>()
    val nameConflict = MutableLiveData<Boolean>()
    val saveSuccess = MutableLiveData<Boolean>()

    val categoryInput = MutableLiveData<Int>()

    val category: LiveData<Category> = Transformations.switchMap(categoryInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Category>()
        data.value = Category()

        return@switchMap data

    }

    private val dao: CategoryDao

    init {
        val app = application as FlexPosApplication
        dao = app.db.categoryDao()
    }

    fun save() {

        var hasErrors = false

        category.value?.also {
            if (!ValidatorUtils.isValid(it.name, ValidatorUtils.NOT_EMPTY)) {
                nameValid.value = false
                hasErrors = true
            }
        }.takeUnless { hasErrors }?.let {
            DaoWorkerAsync<Category>({
                if (dao.findByUniqueNameSync(it.name) != null) {
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