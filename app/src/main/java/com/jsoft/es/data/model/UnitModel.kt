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
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.utils.DaoWorkerAsync

class UnitRepo(private val dao: UnitDao) {

    fun findUnits(sq: Searchable): LiveData<List<Unit>> {
        return dao.findUnits(SimpleSQLiteQuery(sq.query, sq.objects.toTypedArray()))
    }

    fun getUnit(id: Int): LiveData<Unit> {
        return dao.findById(id)
    }

    fun save(unit: Unit) {
        DaoWorkerAsync<Unit>({
            it.uniqueName = it.name.toUpperCase()
            if (it.id > 0) {
                dao.update(it)
            } else {
                dao.insert(it)
            }
        }, {

        }).execute(unit)
    }

    fun delete(unit: Unit) {
        DaoWorkerAsync<Unit>({
            dao.delete(it)
        }, {

        }).execute(unit)
    }

}

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

            name.takeUnless { it.isNullOrBlank() }?.apply {
                sb.append("and UPPER(name) LIKE ? ")
                objects.add(this.toUpperCase())
            }

            return sb.toString()
        }

}

@Dao
interface UnitDao : BaseDao<Unit> {

    @RawQuery(observedEntities = [Unit::class])
    fun findUnits(query: SupportSQLiteQuery): LiveData<List<Unit>>

    @Query("SELECT * FROM unit WHERE id = :id LIMIT 1")
    fun findById(id: Int): LiveData<Unit>

    @Query("SELECT * FROM unit WHERE id = :id LIMIT 1")
    fun findByIdSync(id: Int): Unit

}