package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Sale
import com.jsoft.pos.data.entity.SaleItem

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

    @Transaction
    open fun save(sale: Sale, saleItems: MutableList<SaleItem>) {
        if (sale.id > 0) {
            update(sale)
            saleItems.forEach {
                saleItems.forEach {
                    if (it.id > 0) {
                        updateSaleItem(it)
                    } else {
                        it.saleId = sale.id
                        insertSaleItem(it)
                    }
                }
            }
        } else {
            val id = insertAndGet(sale)
            sale.id = id
            sale.receiptCode = "${10000 + id}"
            update(sale)
            saleItems.forEach {
                it.saleId = id
                insertSaleItem(it)

            }
        }
    }
}