package com.jsoft.pos.ui.views.category

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditCategoryViewModel(app: FlexPosApplication) : AndroidViewModel(app) {

    val categoryInput = MutableLiveData<Int>()

    val category: LiveData<Category> = Transformations.switchMap(categoryInput) {
        if (it > 0) {
            return@switchMap dao.findById(it)
        }

        val data = MutableLiveData<Category>()
        data.value = Category()

        return@switchMap data

    }

    private val dao: CategoryDao = app.db.categoryDao()

    fun save() {
        category.value?.apply {
            DaoWorkerAsync<Category>({
                dao.save(it)
            }, {

            }).execute(this)
        }
    }

}