package com.jsoft.pos.ui.views.category

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.text.TextUtils
import com.jsoft.pos.BR
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditCategoryViewModel(application: Application) : AndroidViewModel(application) {

    val colorChange = MutableLiveData<String>()
    val deleteSuccess = MutableLiveData<Boolean>()

    val nameValid = MutableLiveData<Boolean>()
    val nameNotEmpty = MutableLiveData<Boolean>()
    val nameUnique = MutableLiveData<Boolean>()
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
            nameValid.value = !TextUtils.isEmpty(it.name)
            nameNotEmpty.value = nameValid.value
            if (nameValid.value == false) {
                hasErrors = true
            }
        }.takeUnless { hasErrors }?.also {
            DaoWorkerAsync<Category>({
                val src = dao.findByUniqueNameSync(it.name)
                if (src != null && src.id != it.id ) {
                    nameUnique.postValue(false)
                    nameValid.postValue(false)
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

    fun delete() {
        DaoWorkerAsync<Category>({
            dao.delete(it).let { true }
        }, {
            deleteSuccess.value = true
        }, {
            deleteSuccess.value = false
        }).execute(category.value)
    }

    fun onColorSelect(cs: CharSequence) {
        category.value?.color = cs.toString()
        category.value?.notifyPropertyChanged(BR.color)
        colorChange.value = cs.toString()
    }

}