package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.RawQuery
import android.arch.persistence.room.Transaction
import android.databinding.BaseObservable
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.Searchable
import com.jsoft.pos.data.entity.Category
import com.jsoft.pos.data.entity.CategoryVO

class CategorySearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }

    override val objects: MutableList<Any> = mutableListOf()

    override val query: String
        get() {
            objects.clear()
            val sb = StringBuilder()
            sb.append("SELECT c.id, " +
                    "c.name, " +
                    "c.color, " +
                    "count(i.id) as item_count " +
                    "FROM category c " +
                    "LEFT OUTER JOIN item i ON c.id = i.category_id " +
                    "WHERE 1 = 1 ")

            name.takeUnless { it.isNullOrBlank() }?.apply {
                sb.append("and UPPER(c.name) LIKE ? ")
                objects.add(this.toUpperCase())
            }

            sb.append("GROUP BY c.id ")

            return sb.toString()
        }
}

@Dao
abstract class CategoryDao : BaseDao<Category> {

    @RawQuery(observedEntities = [Category::class])
    abstract fun findCategories(query: SupportSQLiteQuery): LiveData<List<CategoryVO>>

    @Query("SELECT * FROM category")
    abstract fun findAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Category>

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Category

    @Query("SELECT COUNT(*) FROM category")
    abstract fun findCount(): LiveData<Long>

    @Query("SELECT * FROM category WHERE unique_name = :name LIMIT 1")
    abstract fun findByUniqueNameSync(name: String): Category?

    @Transaction
    open fun save(category: Category) {
        category.uniqueName = category.name.toUpperCase()
        if (category.id > 0) {
            update(category)
        } else {
            insert(category)
        }
    }

}