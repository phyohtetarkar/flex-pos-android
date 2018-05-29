package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Tax

abstract class TaxDao : BaseDao<Tax> {

    @Query("SELECT * FROM tax")
    abstract fun findAllTaxes(): LiveData<List<Tax>>

    @Query("SELECT * FROM tax WHERE id IN (SELECT tax_id FROM item_tax WHERE item_id = :itemId)")
    abstract fun findByItemSync(itemId: Long): List<Tax>

    @Query("SELECT * FROM tax WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Tax>

    @Query("SELECT * FROM tax WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Tax

    @Query("SELECT COUNT(*) FROM tax")
    abstract fun findCount(): LiveData<Long>

}