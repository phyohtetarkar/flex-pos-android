package com.jsoft.pos.data.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.jsoft.pos.data.BaseDao
import com.jsoft.pos.data.entity.Discount
import com.jsoft.pos.data.entity.ItemDiscount

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
    open fun save(discount: Discount, itemIds: Collection<Long>?) {
        var id = discount.id
        if (discount.id > 0) {
            update(discount)
        } else {
            id = insertAndGet(discount).toInt()
        }
        val t = findByIdSync(id)

        itemIds?.takeUnless { it.isEmpty() }?.apply { assignDiscount(t, this) }
    }

    protected open fun assignDiscount(discount: Discount, itemIds: Collection<Long>) {
        deleteItemDiscounts(findItemDiscountsSync(discount.id))
        val itemDiscounts = itemIds.map { ItemDiscount(itemId = it, discountId = discount.id) }
        saveItemDiscounts(itemDiscounts)
    }
}