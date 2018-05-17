package com.jsoft.pos.ui.views.category

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import com.jsoft.pos.FluentPosApplication
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.model.CategoryDao
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditCategoryViewModel(application: Application) : AndroidViewModel(application) {

    val category = ObservableField<Category>()

    val categoryInput = MutableLiveData<Int>()

    val categoryLive: LiveData<Category> =
            Transformations.switchMap(categoryInput) { dao.findById(it) }

    private val dao: CategoryDao

    init {
        val app = application as FluentPosApplication
        dao = app.db.categoryDao()
    }

    fun save() {
        category.get()?.apply {
            DaoWorkerAsync<Category>({
                dao.save(it)
            }, {

            }).execute(this)
        }
    }

}