package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.entity.SaleItem
import com.jsoft.pos.data.utils.DaoWorkerAsync

class SaleRepository(
        private val saleDao: SaleDao,
        private val itemRepo: ItemRepository
) {
    fun createSaleFromItemIds(itemIds: LongArray, updateField: MutableLiveData<Sale>, updateList: MutableLiveData<List<SaleItem>>) {

        DaoWorkerAsync<LongArray>({
            val saleItems = it
                    .groupBy { it }
                    .map { en ->
                        val item = itemRepo.getItem(en.key)
                        val saleItem = SaleItem(quantity = en.value.size, price = item.price, itemId = item.id)
                        saleItem.item = item

                        return@map saleItem
                    }

            updateList.postValue(saleItems)
            updateField.postValue(Sale.create(saleItems))
        }, {}, {}).execute(itemIds)

    }
}

@Dao
abstract class SaleDao : BaseDao<Sale> {

    @RawQuery(observedEntities = [Sale::class])
    abstract fun findSales(query: SimpleSQLiteQuery): LiveData<List<Sale>>

    @Query("SELECT * FROM sale WHERE id = :id LIMIT 1")
    abstract fun findById(id: Long): LiveData<Sale>

    @Query("SELECT * FROM sale_item WHERE sale_id = :saleId")
    abstract fun findSaleItemsSync(saleId: Long): List<SaleItem>

    @Insert
    abstract fun insertSaleItem(saleItem: SaleItem)

    @Update
    abstract fun updateSaleItem(saleItem: SaleItem)

    @Delete
    abstract fun deleteSaleItems(saleItems: List<SaleItem>)

    @Transaction
    open fun save(sale: Sale, saleItems: MutableList<SaleItem>) {
        var saleId = sale.id

        if (sale.id > 0) {
            update(sale)
            deleteSaleItems(findSaleItemsSync(saleId))
        } else {
            saleId = insertAndGet(sale)
            sale.id = saleId
            sale.receiptCode = "${10000 + saleId}"
            update(sale)
        }

        saleItems.forEach {
            it.saleId = saleId
            insertSaleItem(it)
        }
    }
}