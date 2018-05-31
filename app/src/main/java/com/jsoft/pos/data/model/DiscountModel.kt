package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.entity.ItemDiscount
import com.jsoft.pos.data.entity.ItemJoinVO

@Dao
abstract class DiscountDao : BaseDao<Discount> {

    @Query("SELECT * FROM discount")
    abstract fun findAllDiscounts(): LiveData<List<Discount>>

    @Query("SELECT i.id as itemId, i.name, it.id as joinedId FROM item i LEFT JOIN item_discount it ON it.item_id = i.id ")
    abstract fun findItemDiscountAssociations(): LiveData<List<ItemJoinVO>>

    @Query("SELECT * FROM discount WHERE id IN (SELECT discount_id FROM item_discount WHERE item_id = :itemId)")
    abstract fun findByItemSync(itemId: Long): List<Discount>

    @Query("SELECT * FROM discount WHERE id = :id LIMIT 1")
    abstract fun findById(id: Int): LiveData<Discount>

    @Query("SELECT * FROM discount WHERE id = :id LIMIT 1")
    abstract fun findByIdSync(id: Int): Discount

    @Query("SELECT COUNT(*) FROM discount")
    abstract fun findCount(): LiveData<Long>

    @Query("SELECT * FROM item_discount WHERE discount_id = :discountId")
    protected abstract fun findItemDiscountsSync(discountId: Int): List<ItemDiscount>

    @Insert
    protected abstract fun saveItemDiscounts(itemDiscounts: List<ItemDiscount>)

    @Delete
    protected abstract fun deleteItemDiscounts(itemDiscounts: List<ItemDiscount>)

    @Transaction
    open fun save(discount: Discount) {
        if (discount.id > 0) {
            update(discount)
        } else {
            insert(discount)
        }
    }

    @Transaction
    open fun save(discount: Discount, items: MutableList<ItemJoinVO>?) {
        var id = discount.id
        if (discount.id > 0) {
            update(discount)
        } else {
            id = insertAndGet(discount).toInt()
        }
        val t = findByIdSync(id)
        items?.apply { assignDiscount(t, this) }
    }

    @Transaction
    protected open fun assignDiscount(discount: Discount, items: MutableList<ItemJoinVO>) {
        deleteItemDiscounts(findItemDiscountsSync(discount.id))
        val itemDiscounts = items.map { ItemDiscount(itemId = it.itemId, discountId = discount.id) }
        saveItemDiscounts(itemDiscounts)
    }
}