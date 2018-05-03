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
import com.jsoft.es.data.entity.Currency
import com.jsoft.es.data.entity.Unit
import com.jsoft.es.data.utils.DaoWorkerAsync

class CurrencyModelRepo(private val dao: CurrencyDao) {

    fun findUnits(sq: Searchable): LiveData<List<Currency>> {
        return dao.findCurrencies(SimpleSQLiteQuery(sq.query, sq.objects.toTypedArray()))
    }

    fun getUnit(id: Int): LiveData<Currency> {
        return dao.findById(id)
    }

    fun save(currency: Currency) {
        DaoWorkerAsync<Currency>({ c ->
            c.uniqueName = c.name.toUpperCase()
            if (c.id > 0) {
                dao.update(c)
            } else {
                dao.insert(c)
            }
        }, {

        }).execute(currency)
    }

    fun delete(currency: Currency) {
        DaoWorkerAsync<Currency>({ c ->
            dao.delete(c)
        }, {

        }).execute(currency)
    }

}

class CurrencySearch : BaseObservable(), Searchable {

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
interface CurrencyDao : BaseDao<Currency> {

    @RawQuery(observedEntities = [Currency::class])
    fun findCurrencies(query: SupportSQLiteQuery): LiveData<List<Currency>>

    @Query("SELECT * FROM currency WHERE id = :id LIMIT 1")
    fun findById(id: Int): LiveData<Currency>

    @Query("SELECT * FROM currency WHERE id = :id LIMIT 1")
    fun findByIdSync(id: Int): Currency

}