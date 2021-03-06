package com.flex.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.room.*
import android.databinding.BaseObservable
import com.flex.pos.data.BaseDao
import com.flex.pos.data.Searchable
import com.flex.pos.data.Searchable.Companion.BASE_QUERY
import com.flex.pos.data.entity.Sale
import com.flex.pos.data.entity.SaleItem
import com.flex.pos.data.utils.DaoWorkerAsync
import java.util.*

class SaleRepository(
        private val saleDao: SaleDao,
        private val itemRepo: ItemRepository? = null
) {
    fun initSale(saleItems: List<SaleItem>): Sale? {
        saleItems.forEach { si ->
            if (si.item == null) {
                si.item = itemRepo?.getItem(si.itemId)
            }
        }

        val sale = Sale()
        sale.saleItems = saleItems.toMutableList()

        return sale

    }

    fun getSale(id: Long): LiveData<Sale> {
        val liveSale = MutableLiveData<Sale>()

        DaoWorkerAsync<Long>({

            if (it > 0) {
                liveSale.postValue(getSaleSync(it))
            }

            return@DaoWorkerAsync true

        }, {}, {}).execute(id)

        return liveSale
    }

    fun getSaleSync(id: Long): Sale? {

        if (id > 0) {
            val sale = saleDao.findByIdSync(id)
            val saleItems = saleDao.findSaleItemsSync(id)
            saleItems.forEach {
                it.item = itemRepo?.getItem(it.itemId)
            }

            sale.saleItems = saleItems.toMutableList()

            return sale
        }

        return null
    }

    fun findSales(search: SaleSearch): LiveData<PagedList<Sale>> {
        return LivePagedListBuilder(saleDao.findSales(SimpleSQLiteQuery(search.query, search.objects.toTypedArray())), 20).build()
    }
}

class SaleSearch : BaseObservable(), Searchable {

    override val query: String
        get() {
            val sb = StringBuilder(String.format(BASE_QUERY, "sale"))
            sb.append("ORDER BY sale.issue_date desc ")
            objects.clear()

            return sb.toString()
        }
    override val objects: MutableList<Any> = mutableListOf()

}

@Dao
abstract class SaleDao : BaseDao<Sale> {

    @RawQuery(observedEntities = [Sale::class])
    abstract fun findSales(query: SimpleSQLiteQuery): DataSource.Factory<Int, Sale>

    @Query("SELECT * FROM sale WHERE id = :id LIMIT 1")
    abstract fun findById(id: Long): LiveData<Sale>

    @Query("SELECT * FROM sale WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Long): Sale

    @Query("SELECT * FROM sale_item WHERE sale_id = :saleId")
    abstract fun findSaleItemsSync(saleId: Long): List<SaleItem>

    @Insert
    abstract fun insertSaleItem(saleItem: SaleItem)

    @Update
    abstract fun updateSaleItem(saleItem: SaleItem)

    @Delete
    abstract fun deleteSaleItems(saleItems: List<SaleItem>)

    @Transaction
    open fun save(sale: Sale) {
        var saleId = sale.id

        if (sale.id > 0) {
            update(sale)
            deleteSaleItems(findSaleItemsSync(saleId))
        } else {
            sale.issueDate = Date()
            saleId = insertAndGet(sale)
            sale.id = saleId
            sale.receiptCode = "${10000 + saleId}"
            update(sale)
        }

        sale.saleItems.forEach {
            it.saleId = saleId
            insertSaleItem(it)
        }
    }
}