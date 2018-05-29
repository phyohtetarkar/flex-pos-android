package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Discount

@Dao
abstract class DiscountDao : BaseDao<Discount> {

    @Query("SELECT * FROM discount")
    abstract fun findAllDiscounts(): LiveData<List<Discount>>

    @Query("SELECT * FROM discount WHERE id IN (SELECT discount_id FROM item_discount WHERE item_id = :itemId)")
    abstract fun findByItemSync(itemId: Long): List<Discount>

    @Query("SELECT * FROM discount WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Discount>

    @Query("SELECT * FROM discount WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Discount

    @Query("SELECT COUNT(*) FROM discount")
    abstract fun findCount(): LiveData<Long>

}