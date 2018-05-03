package com.jsoft.es.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.RawQuery
import android.databinding.BaseObservable
import com.jsoft.es.data.BaseDao
import com.jsoft.es.data.Searchable
import com.jsoft.es.data.entity.Category
import com.jsoft.es.data.entity.CategoryVO
import com.jsoft.es.data.utils.DaoWorkerAsync

class CategoryRepo(private val dao: CategoryDao) {

    fun findCategories(sq: Searchable): LiveData<List<CategoryVO>> {
        return dao.findCategories(SimpleSQLiteQuery(sq.query, sq.objects.toTypedArray()))
    }

    fun getCategory(id: Int): LiveData<Category> {
        return dao.findById(id)
    }

    fun save(category: Category) {
        DaoWorkerAsync<Category>({
            it.uniqueName = it.name.toUpperCase()
            if (it.id > 0) {
                dao.update(it)
            } else {
                dao.insert(it)
            }
        }, {

        }).execute(category)
    }

}

class CategorySearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }

    override val objects: MutableList<Any> = mutableListOf()

    override val query: String
        get() {
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
interface CategoryDao : BaseDao<Category> {

    @RawQuery(observedEntities = [Category::class])
    fun findCategories(query: SupportSQLiteQuery): LiveData<List<CategoryVO>>

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    fun findById(id: Int): LiveData<Category>

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    fun findByIdSync(id: Int): Category

}