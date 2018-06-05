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
import com.jsoft.pos.data.entity.Unit

class UnitSearch : BaseObservable(), Searchable {

    var name: String? = null
        set(name) {
            field = name
            notifyChange()
        }

    override val objects: MutableList<Any> = mutableListOf()

    override val query: String
        get() {
            val sb = StringBuilder(String.format(Searchable.BASE_QUERY, Unit::class.java.simpleName))
            objects.clear()
            name.takeUnless { it.isNullOrBlank() }?.apply {
                sb.append("and UPPER(name) LIKE ? ")
                objects.add(this.toUpperCase())
            }

            return sb.toString()
        }

}

@Dao
abstract class UnitDao : BaseDao<Unit> {

    @RawQuery(observedEntities = [Unit::class])
    abstract fun findUnits(query: SupportSQLiteQuery): LiveData<List<Unit>>

    @Query("SELECT * FROM unit")
    abstract fun findAllUnits(): LiveData<List<Unit>>

    @Query("SELECT * FROM unit WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Unit>

    @Query("SELECT * FROM unit WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Unit

    @Query("SELECT COUNT(*) FROM unit")
    abstract fun findCount(): LiveData<Long>

    @Transaction
    open fun save(unit: Unit) {
        unit.uniqueName = unit.name.toUpperCase()
        if (unit.id > 0) {
            update(unit)
        } else {
            insert(unit)
        }
    }

}