package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.RawQuery
import android.arch.persistence.room.Transaction
import android.databinding.BaseObservable
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.Searchable
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.Item
import com.jsoft.pos.data.entity.ItemVO
import com.jsoft.pos.data.entity.Unit

class ItemRepository(
        private val dao: ItemDao,
        private val unitDao: UnitDao? = null,
        private val categoryDao: CategoryDao? = null
) {

    fun findItemsCheckedWithTax(search: ItemSearch, taxId: Int, checkedIds: Collection<Long>?): List<Item> {

        val items = dao.findItems(SimpleSQLiteQuery(search.query, search.objects.toTypedArray()))
        val itemsFiltered: List<Item>? = taxId.let {
            if (it > 0) {
                dao.findItemTaxAssociations(it)
            } else {
                null
            }

        }

        items.forEach {
            if (!checkedIds.orEmpty().isEmpty()) {
                it._checked = checkedIds.orEmpty().contains(it.id)
            } else {
                it._checked = itemsFiltered.orEmpty().contains(it)
            }
        }


        return items
    }

    fun findItemsCheckedWithDiscount(search: ItemSearch, discountId: Int, checkedIds: Collection<Long>?): List<Item> {

        val items = dao.findItems(SimpleSQLiteQuery(search.query, search.objects.toTypedArray()))
        val itemsFiltered: List<Item>? = discountId.let {
            if (it > 0) {
                dao.findItemDiscountAssociations(discountId)
            } else {
                null
            }
        }

        items.forEach {
            if (!checkedIds.orEmpty().isEmpty()) {
                it._checked = checkedIds.orEmpty().contains(it.id)
            } else {
                it._checked = itemsFiltered.orEmpty().contains(it)
            }
        }

        return items
    }

    fun findItemVOs(search: ItemVOSearch): LiveData<PagedList<ItemVO>> {
        return LivePagedListBuilder(dao.findItemVOs(SimpleSQLiteQuery(search.query, search.objects.toTypedArray())), 20).build()
    }

    fun getItem(id: Long): Item {
        if (id > 0) {
            val item = dao.findByIdSync(id)
            item.categoryId?.apply {
                item.category = categoryDao?.findByIdSync(this)
            }

            item.unitId?.apply {
                item.unit = unitDao?.findByIdSync(this)
            }

            return item
        }

        return Item()
    }

    fun save(item: Item) {
        dao.save(item)
    }

    fun delete(item: Item) {
        dao.delete(item)
    }

}

class ItemSearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }

    override val query: String
        get() {
            val sb = StringBuilder()

            sb.append("SELECT * FROM item WHERE 1 = 1 ")

            name?.apply {
                sb.append("AND UPPER(i.name) LIKE ? ")
                objects.add("${toUpperCase()}%")
            }

            return sb.toString()
        }

    override val objects: MutableList<Any> = mutableListOf()

}

class ItemVOSearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }

    var categoryId: Int = 0

    var isNotAvailable: Boolean = false
        set(notAvailable) {
            field = notAvailable
            notifyChange()
        }

    var isAvailable: Boolean = false
        set(notAvailable) {
            field = notAvailable
            notifyChange()
        }

    override val objects: MutableList<Any> = mutableListOf()

    override val query: String
        get() {
            val sb = StringBuilder()
            sb.append("SELECT i.id, " +
                    "i.name, " +
                    "i.barcode, " +
                    "i.image, " +
                    "u.name as unit, " +
                    "c.name as category, " +
                    "c.color, " +
                    "i.amount, " +
                    "i.price " +
                    "FROM item i " +
                    "LEFT OUTER JOIN unit u ON u.id = i.unit_id " +
                    "LEFT OUTER JOIN category c ON c.id = i.category_id " +
                    "WHERE 1 = 1 ")

            name?.apply {
                sb.append("and UPPER(c.name) LIKE ? ")
                objects.add("${toUpperCase()}%")
            }

            categoryId.takeUnless { it == 0 }?.apply {
                sb.append("and i.category_id = ? ")
                objects.add(this)
            }

            if (isNotAvailable) {
                sb.append("and i.available = ? ")
                objects.add(false)
            }

            if (isAvailable) {
                sb.append("and i.available = ? ")
                objects.add(true)
            }

            return sb.toString()
        }

}

@Dao
abstract class ItemDao : BaseDao<Item> {

    @RawQuery(observedEntities = [Item::class, Category::class, Unit::class])
    abstract fun findItemVOs(query: SupportSQLiteQuery): DataSource.Factory<Int, ItemVO>

    @RawQuery
    abstract fun findItems(query: SupportSQLiteQuery): List<Item>

    @Query("SELECT i.* FROM item i " +
            "INNER JOIN item_tax it ON it.item_id = i.id " +
            "WHERE it.tax_id = :taxId ")
    abstract fun findItemTaxAssociations(taxId: Int): List<Item>

    @Query("SELECT i.* FROM item i " +
            "INNER JOIN item_discount id ON id.item_id = i.id " +
            "WHERE id.discount_id = :discountId ")
    abstract fun findItemDiscountAssociations(discountId: Int): List<Item>

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    abstract fun findById(id: Long): LiveData<Item>

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Long): Item

    @Query("SELECT COUNT(*) FROM item")
    abstract fun findCount(): LiveData<Long>

    @Transaction
    open fun save(item: Item) {
        if (item.id > 0) {
            update(item)
        } else {
            insert(item)
        }
    }

}