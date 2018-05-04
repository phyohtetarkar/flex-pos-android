package com.jsoft.es.data.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import android.databinding.BaseObservable
import android.util.Log
import com.jsoft.es.data.BaseDao
import com.jsoft.es.data.Searchable
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.Item
import com.jsoft.es.data.entity.ItemVO
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.utils.DaoWorkerAsync

class ItemRepo(private val dao: ItemDao) {

    private lateinit var unitDao: UnitDao
    private lateinit var categoryDao: CategoryDao

    constructor(
            dao: ItemDao,
            unitDao: UnitDao,
            categoryDao: CategoryDao
    ) : this(dao) {

        this.unitDao = unitDao
        this.categoryDao = categoryDao

    }

    fun findItems(sq: Searchable): LiveData<PagedList<ItemVO>> {
        val query = SimpleSQLiteQuery(sq.query, sq.objects.toTypedArray())

        return LivePagedListBuilder(dao.findItems(query), 20).build()
    }

    fun getItem(id: Long): LiveData<Item> {
        return dao.findById(id)
    }

    fun getItemWithRelation(id: Long): LiveData<Item> {
        val itemLiveData = MutableLiveData<Item>()
        DaoWorkerAsync<Long>({
            val item = dao.findByIdSync(id)
            item.category = categoryDao.findByIdSync(item.categoryId)

            itemLiveData.value = item
        }, {

        }).execute(id)

        return itemLiveData
    }

    fun save(item: Item) {
        DaoWorkerAsync<Item>({
            if (it.id > 0) {
                dao.update(it)
            } else {
                val id = dao.insertAndGet(it)
                val v = dao.findByIdSync(id)
                v.code = "${10000 + id}"
                dao.update(v)
            }
        }, {

        }).execute(item)

    }

    fun delete(item: Item) {
        DaoWorkerAsync<Item>({
            dao.delete(it)
        }, {

        }).execute(item)
    }

}

class ItemSearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }
    var categoryId: Int = 0
    var isDiscount: Boolean = false
        set(discount) {
            field = discount
            notifyChange()
        }
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
                    "i.code, " +
                    "i.image, " +
                    "i.amount, " +
                    "i.discount_flag, " +
                    "i.retail_price, " +
                    "i.discount, " +
                    "u.name as unit, " +
                    "c.name as category, " +
                    "c.color " +
                    "FROM item i " +
                    "LEFT OUTER JOIN unit u ON u.id = i.unit_id " +
                    "LEFT OUTER JOIN category c ON c.id = i.category_id " +
                    "WHERE 1 = 1 ")

            name.takeUnless { it.isNullOrBlank() }?.apply {
                sb.append("and UPPER(c.name) LIKE ? ")
                objects.add(this.toUpperCase())
            }

            categoryId.takeUnless { it == 0 }?.apply {
                sb.append("and i.category_id = ? ")
                objects.add(this)
            }

            if (isDiscount) {
                sb.append("and i.discount_flag = ? ")
                objects.add(true)
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
interface ItemDao : BaseDao<Item> {

    @RawQuery(observedEntities = [Item::class, Category::class, Unit::class])
    fun findItems(query: SupportSQLiteQuery): DataSource.Factory<Int, ItemVO>

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    fun findById(id: Long): LiveData<Item>

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    fun findByIdSync(id: Long): Item

}