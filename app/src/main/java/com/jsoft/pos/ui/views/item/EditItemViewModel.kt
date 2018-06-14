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

    val itemInput = MutableLiveData<Long>()

    val itemSaved = MutableLiveData<Boolean>()
    val itemDeleted = MutableLiveData<Boolean>()

    val item: LiveData<Item> = Transformations.switchMap(itemInput) {
        val liveItem = MutableLiveData<Item>()

        DaoWorkerAsync<Long>({
            liveItem.postValue(repository.getItem(it)).let { true }
        },{},{}).execute(it)

        return@switchMap liveItem
    }

    val categories: LiveData<List<Category>> by lazy { categoryDao.findAllCategories() }
    val units: LiveData<List<Unit>> by lazy { unitDao.findAllUnits() }

    val taxes: LiveData<List<Tax>> = Transformations.switchMap(itemInput) {
        val liveTaxes = MutableLiveData<List<Tax>>()
        DaoWorkerAsync<Long>({
            liveTaxes.postValue(taxDao.findItemTaxAssociations(it))
            return@DaoWorkerAsync true
        },{},{}).execute(it)
        return@switchMap liveTaxes
    }

    val discounts: LiveData<List<Discount>> = Transformations.switchMap(itemInput) {
        val liveDiscounts = MutableLiveData<List<Discount>>()
        DaoWorkerAsync<Long>({
            liveDiscounts.postValue(discountDao.findItemDiscountAssociations(it)).let { true }
        },{},{}).execute(it)
        return@switchMap liveDiscounts
    }

    private val repository: ItemRepository

    private val categoryDao: CategoryDao
    private val unitDao: UnitDao
    private val taxDao: TaxDao
    private val discountDao: DiscountDao

    init {
        val app = application as FlexPosApplication
        categoryDao = app.db.categoryDao()
        unitDao = app.db.unitDao()
        repository = ItemRepository(app.db.itemDao(), unitDao, categoryDao)
        taxDao = app.db.taxDao()
        discountDao = app.db.discountDao()
    }

    fun save() {
        DaoWorkerAsync<Item>({
            repository.save(it, taxes.value, discounts.value).let { true }
        },{

        },{
            itemSaved.value = true
        }).execute(item.value)
    }

    fun delete() {
        DaoWorkerAsync<Item>({
            repository.delete(it).let { true }
        },{

        },{
            itemDeleted.value = true
        }).execute(item.value)
    }

}