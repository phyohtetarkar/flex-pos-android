package com.jsoft.es.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.RawQuery
import com.jsoft.es.data.BaseDao
import com.jsoft.es.data.entity.Sale

@Dao
interface SaleDao : BaseDao<Sale> {

    @RawQuery(observedEntities = [Sale::class])
    fun findSales(query: SimpleSQLiteQuery): LiveData<List<Sale>>

    @Query("SELECT * FROM sale WHERE id = :id LIMIT 1")
    fun findById(id: Int): LiveData<Sale>

}