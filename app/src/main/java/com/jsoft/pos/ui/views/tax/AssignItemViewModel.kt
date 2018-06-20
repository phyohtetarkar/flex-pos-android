package com.jsoft.pos.ui.views.tax

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.Item
import com.jsoft.pos.data.model.ItemRepository
import com.jsoft.pos.data.model.ItemSearch
import com.jsoft.pos.data.utils.DaoWorkerAsync
import com.jsoft.pos.data.utils.ObserverMutableLiveData

class AssignItemViewModel(application: Application) : AndroidViewModel(application) {

    val search = ObserverMutableLiveData<ItemSearch>()
    var id = 0
    var checkedIds: Collection<Long>? = null
    lateinit var type: Item.AssignType

    val items: LiveData<List<Item>> = Transformations.switchMap(search) {
        val liveItems = MutableLiveData<List<Item>>()

        DaoWorkerAsync<ItemSearch>({
            liveItems.postValue(repository.findItemsChecked(it, id, checkedIds, type)).let { true }
        }, {}, {}).execute(it)

        return@switchMap liveItems

    }

    private val repository: ItemRepository

    init {
        val app = application as FlexPosApplication
        repository = ItemRepository(app.db!!.itemDao())
    }

}