package com.jsoft.es.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableField
import com.jsoft.es.EasyShopApplication
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.Item
import com.jsoft.es.data.model.CategoryRepo
import com.jsoft.es.data.model.ItemRepo

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    val item = ObservableField<Item>()

    val itemInput = MutableLiveData<Long>()
    val categoryInput = MutableLiveData<Int>()

    val itemLiveData: LiveData<Item> =
            Transformations.switchMap(itemInput) { repo.getItem(it) }

    val categoryLiveData: LiveData<Category> =
            Transformations.switchMap(categoryInput) { categoryRepo.getCategory(it) }

    val categories: LiveData<List<Category>>;

    private val repo: ItemRepo
    private val categoryRepo: CategoryRepo

    init {
        val app = application as EasyShopApplication
        repo = ItemRepo(app.db.itemDao())
        categoryRepo = CategoryRepo(app.db.categoryDao())

        categories = categoryRepo.findAllCategories()
    }

    fun save() {
        item.get()?.apply { repo.save(this) }
    }

    fun delete() {
        item.get()?.apply { repo.delete(this) }
    }

}