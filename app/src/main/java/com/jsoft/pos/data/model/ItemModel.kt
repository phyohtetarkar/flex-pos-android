package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
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
import com.jsoft.pos.data.utils.DaoWorkerAsync

class ItemService(
        private val dao: ItemDao,
        private val unitDao: UnitDao,
        private val categoryDao: CategoryDao,
        private val taxDao: TaxDao,
        private val discountDao: DiscountDao
) {

    fun getItem(id: Long): Item {
        return if (id > 0) {
            val item = dao.findByIdSync(id)
            item.categoryId?.apply {
                item.category = categoryDao.findByIdSync(this)
            }

            item.unitId?.apply {
                item.unit = unitDao.findByIdSync(this)
            }

            item.taxes = taxDao.findByItemSync(id).toMutableList()
            item.discounts = discountDao.findByItemSync(id).toMutableList()

            item
        } else {
            Item()
        }
    }

    fun save(item: Item?) {
        item?.apply {
            DaoWorkerAsync<Item>({
                dao.save(it)
            }, {

            }).execute(this)
        }

    }

    fun delete(item: Item?) {
        item?.apply {
            DaoWorkerAsync<Item>({
                dao.delete(it)
            }, {

            }).execute(this)
        }
    }

}

class ItemSearch : Searchable {

    var name: String? = null

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
    abstract fun findItems(query: SupportSQLiteQuery): DataSource.Factory<Int, ItemVO>

    @RawQuery(observedEntities = [Item::class, Category::class, Unit::class])
    abstract fun findItemsByName(query: SupportSQLiteQuery): LiveData<List<Item>>

    @Query("SELECT * FROM item i " +
            "INNER JOIN item_tax it ON it.item_id = i.id " +
            "WHERE it.tax_id = :taxId ")
    abstract fun findItemTaxAssociations(taxId: Int): LiveData<List<Item>>

    @Query("SELECT * FROM item i " +
            "INNER JOIN item_discount it ON it.discount_id = i.id " +
            "WHERE it.discount_id = :discountId ")
    abstract fun findItemDiscountAssociations(discountId: Int): LiveData<List<Item>>

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