package com.jsoft.es.data.model

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.RawQuery
import android.arch.persistence.room.Transaction
import android.databinding.BaseObservable
import com.jsoft.es.data.BaseDao
import com.jsoft.es.data.Searchable
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.Item
import com.jsoft.es.data.entity.ItemVO
import com.jsoft.es.data.entity.Unit
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.apply
import kotlin.takeUnless

class ItemSearch : BaseObservable(), Searchable {

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
                    "i.code, " +
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

            name.takeUnless { it.isNullOrBlank() }?.apply {
                sb.append("and UPPER(c.name) LIKE ? ")
                objects.add(this.toUpperCase())
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

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    abstract fun findById(id: Long): LiveData<Item>

    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Long): Item

    @Transaction
    open fun save(item: Item) {
        if (item.id > 0) {
            update(item)
        } else {
            insert(item)
        }
    }
}