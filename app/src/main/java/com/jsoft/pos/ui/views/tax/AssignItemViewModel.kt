package com.jsoft.pos.ui.views.tax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Item
import com.jsoft.pos.data.model.ItemRepository
import com.jsoft.pos.data.model.ItemSearch
import com.jsoft.pos.data.utils.SearchMutableLiveData

class AssignItemViewModel(application: Application) : AndroidViewModel(application) {

    val search = SearchMutableLiveData<ItemSearch>()
    var id = 0
    lateinit var type: AssignItemActivity.AssignType

    val items: LiveData<List<Item>> = Transformations.switchMap(search) {
        when (type) {
            AssignItemActivity.AssignType.TAX -> {
                repository.findItemsCheckedWithTax(it, id)
            }
            AssignItemActivity.AssignType.DISCOUNT -> {
                repository.findItemsCheckedWithDiscount(it, id)
            }
        }

    }

    private val repository: ItemRepository

    init {
        val app = application as FlexPosApplication
        repository = ItemRepository(app.db.itemDao())
    }

}