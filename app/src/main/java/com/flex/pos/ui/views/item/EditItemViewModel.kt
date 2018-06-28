package com.flex.pos.ui.views.item

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.text.TextUtils
import com.flex.pos.FlexPosApplication
import com.flex.pos.data.entity.*
import com.flex.pos.data.entity.Unit
import com.flex.pos.data.model.*
import com.flex.pos.data.utils.DaoWorkerAsync

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    val itemInput = MutableLiveData<Long>()

    val nameNotEmpty = MutableLiveData<Boolean>()
    val categoryNotEmpty = MutableLiveData<Boolean>()
    val unitNotEmpty = MutableLiveData<Boolean>()
    val amountValid = MutableLiveData<Boolean>()
    val priceValid = MutableLiveData<Boolean>()

    val saveSuccess = MutableLiveData<Boolean>()
    val deleteSuccess = MutableLiveData<Boolean>()

    val item: LiveData<Item> = Transformations.switchMap(itemInput) {
        val liveItem = MutableLiveData<Item>()

        DaoWorkerAsync<Long>({
            val item = repository.getItem(it)
            return@DaoWorkerAsync liveItem.postValue(item).let { true }
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
        var hasError = false

        item.value?.apply {
            nameNotEmpty.value = !TextUtils.isEmpty(name)

            if (nameNotEmpty.value == false) {
                hasError = true
            }
        }?.apply {
            categoryNotEmpty.value = (categoryId != null && categoryId!! > 0)
            if (categoryNotEmpty.value == false) {
                hasError = true
            }
        }?.apply {
            unitNotEmpty.value = (unitId != null && unitId!! > 0)
            if (unitNotEmpty.value == false) {
                hasError = true
            }
        }?.apply {
            amountValid.value = (amount > 0)
            if (amountValid.value == false) {
                hasError = true
            }
        }?.apply {
            priceValid.value = (price > 0)
            if (priceValid.value == false) {
                hasError = true
            }
        }.takeUnless { hasError }?.also {
            DaoWorkerAsync<Item>({
                return@DaoWorkerAsync repository.save(it, taxes.value, discounts.value).let { true }
            },{
                saveSuccess.value = true
            },{
                saveSuccess.value = false
            }).execute(it)
        }

    }

    fun delete() {
        DaoWorkerAsync<Item>({
            repository.delete(it).let { true }
        },{
            deleteSuccess.value = true
        },{
            deleteSuccess.value = false
        }).execute(item.value)
    }

}