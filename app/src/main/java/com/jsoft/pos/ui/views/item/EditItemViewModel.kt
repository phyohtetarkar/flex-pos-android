package com.jsoft.pos.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.jsoft.pos.FlexPosApplication
import com.jsoft.pos.data.entity.*
import com.jsoft.pos.data.entity.Unit
import com.jsoft.pos.data.model.*
import com.jsoft.pos.data.utils.DaoWorkerAsync

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    //val item = ObservableField<Item>()

    val itemInput = MutableLiveData<Long>()

    val item: LiveData<Item> = Transformations.switchMap(itemInput) {
        val live = MutableLiveData<Item>()

        DaoWorkerAsync<Long>({
            live.postValue(service.getItem(it))
        }, {

        }).execute(it)

        return@switchMap live
    }

    val categories: LiveData<List<Category>> by lazy { categoryDao.findAllCategories() }
    val units: LiveData<List<Unit>> by lazy { unitDao.findAllUnits() }
    val taxes: LiveData<List<Tax>> by lazy { taxDao.findAllTaxes() }
    val discounts: LiveData<List<Discount>> by lazy { discountDao.findAllDiscounts() }

    private val service: ItemService
    private val categoryDao: CategoryDao
    private val unitDao: UnitDao
    private val taxDao: TaxDao
    private val discountDao: DiscountDao

    init {
        val app = application as FlexPosApplication
        categoryDao = app.db.categoryDao()
        unitDao = app.db.unitDao()
        taxDao = app.db.taxDao()
        discountDao = app.db.discountDao()
        service = ItemService(app.db.itemDao(), unitDao, categoryDao,
                taxDao, discountDao)
    }

    fun save() {
        service.save(item.value)
    }

    fun delete() {
        service.delete(item.value)
    }

}