package com.jsoft.es.ui.views.category

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.model.CategoryRepo

class EditCategoryViewModel(application: Application) : AndroidViewModel(application) {

    val category = ObservableField<Category>()

    val categoryInput = MutableLiveData<Int>()

    val categoryLive: LiveData<Category> =
            Transformations.switchMap(categoryInput) { repo.getCategory(it) }

    private val repo: CategoryRepo

    init {
        val app = application as EasyShopApplication
        repo = CategoryRepo(app.db.categoryDao())
    }

    fun save() {
        category.get()?.apply { repo.save(this) }
    }

}